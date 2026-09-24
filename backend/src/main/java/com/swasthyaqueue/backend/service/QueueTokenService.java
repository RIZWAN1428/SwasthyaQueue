package com.swasthyaqueue.backend.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.swasthyaqueue.backend.entity.Department;
import com.swasthyaqueue.backend.entity.DepartmentPrerequisite;
import com.swasthyaqueue.backend.entity.Patient;
import com.swasthyaqueue.backend.entity.QueueToken;
import com.swasthyaqueue.backend.entity.TokenStatus;
import com.swasthyaqueue.backend.repository.DepartmentPrerequisiteRepository;
import com.swasthyaqueue.backend.repository.DepartmentRepository;
import com.swasthyaqueue.backend.repository.PatientRepository;
import com.swasthyaqueue.backend.repository.QueueTokenRepository;

@Service 
public class QueueTokenService {
    
    @Autowired
    private QueueTokenRepository queueTokenRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired 
    private DepartmentRepository departmentRepository;

    //Inject Redis template
    @Autowired 
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private DepartmentPrerequisiteRepository departmentPrerequisiteRepository;

    //Create a Token
    public QueueToken createToken(Long patientId, Long departmentId, Integer severityScore){

        Optional<Patient> patientResult = patientRepository.findById(patientId);
        if(patientResult.isEmpty()){
            throw new IllegalArgumentException("No patient found with id: " +patientId);
        }
        Optional<Department> departmentResult = departmentRepository.findById(departmentId);
        if(departmentResult.isEmpty()){
            throw new IllegalArgumentException("No department found with id: " +patientId);
        }

        Patient patient = patientResult.get();
        Department department = departmentResult.get();
        //check if any prerequisites available before creating token.
        validatePrerequisitesCompleted(patient, department);

        String tokenNumber = generateTokenNumber(department);

        QueueToken newToken = new QueueToken(patient, department, severityScore, tokenNumber);

        return queueTokenRepository.save(newToken);

    }
    //generate a Token number
    private String generateTokenNumber(Department department){
       
        long nextNumber = queueTokenRepository.getNextTokenSequeValue();

        String prefix = department.getName().substring(0,1).toUpperCase();

        return prefix + "-" +String.format("%03d", nextNumber);
    }
    //get Queue for department wise
    public List<QueueToken> getQueueForDepartment(Long departmentId){

        Optional<Department> departmentResult = departmentRepository.findById(departmentId);
        if(departmentResult.isEmpty()){
            throw new IllegalArgumentException("No department found with id: " +departmentId);
        }

        Department department = departmentResult.get();
        List<QueueToken> waitingTokens = queueTokenRepository.findByDepartmentAndStatus(department, TokenStatus.WAITING);

        for(QueueToken token : waitingTokens){
            double score = calculatePriorityScore(token);
            token.setPriorityScore(score);

            String redisKey = buildRedisKey(token.getId());
            redisTemplate.opsForValue().set(redisKey, String.valueOf(score));
        }

        waitingTokens.sort((a,b) -> Double.compare(b.getPriorityScore(), a.getPriorityScore()));

        //Calculate wait timing per department for each patients.
        calculateWaitTimesAndAlerts(waitingTokens, department);
        return waitingTokens;
    }
    //Build a Calculate priority Score
    private static final double SEVERITY_WEIGHT = 10.0;
    private static final double AGING_WEIGHT = 0.3;
    private double calculatePriorityScore(QueueToken token){
        long minutesWaited = Duration.between(token.getArrivalTimestamp(), LocalDateTime.now()).toMinutes();

        return (token.getSeverityScore() * SEVERITY_WEIGHT) + (minutesWaited + AGING_WEIGHT);
    }
    
    //Get all tokens for department
    public List<QueueToken> getAllTokensForDepartment(Long departmentId){
        
        Optional<Department> departmentResult = departmentRepository.findById(departmentId);
        if(departmentResult.isEmpty()){
            throw new IllegalArgumentException("No department found with id: " +departmentId);
        }
        Department department = departmentResult.get();
        List<QueueToken> allTokens = queueTokenRepository.findByDepartment(department);

        int peopleAhead = 0;
        for(QueueToken token : allTokens){
            if(token.getStatus()  == TokenStatus.WAITING){
                double avgTimePerPatient = (department.getAvgTime() != null) ? department.getAvgTime() : 10.0;
                double estimatedWait = peopleAhead * avgTimePerPatient;
                token.setEstimatedWaitMinutes(estimatedWait);
                token.setShouldAlert(estimatedWait <= 15);
                peopleAhead++;
            }
        }

        return allTokens;
    }
    
    //Schedule method for write value in postgres from redis at 30 sec
    @Scheduled(fixedRate = 30000)
    public void syncPriorityScoresToDatabase(){

       
        List<QueueToken> allWaitingTokens = queueTokenRepository.findByStatus(TokenStatus.WAITING);
    
        for(QueueToken token : allWaitingTokens){

            String redisKey = buildRedisKey(token.getId());
            Object cachedScore = redisTemplate.opsForValue().get(redisKey);
            
            //maybe queue was never view since the token was created
            if(cachedScore != null){
                //convert back from redis plain text to number
                double score = Double.parseDouble(cachedScore.toString());
                token.setPriorityScore(score);
                queueTokenRepository.save(token);
                
            }
        }
    }
    //Redis Key
    private String buildRedisKey(Long tokenId) {
        return "queue:token:priority:" + tokenId;
    }

    //Check in prerequistes before creating token
    private void validatePrerequisitesCompleted(Patient patient, Department department){

        List<DepartmentPrerequisite> prerequisites = departmentPrerequisiteRepository.findByDepartment(department);

        for(DepartmentPrerequisite prerequisite : prerequisites){

            Department requiredDept = prerequisite.getPrerequisiteDepartment();

            boolean completed = queueTokenRepository.existsByPatientAndDepartmentAndStatus(patient, requiredDept, TokenStatus.COMPLETED);

            if(!completed){
                throw new IllegalArgumentException(" Patient must complete " + requiredDept.getName() + " before Joining " + department.getName());
            }
        }
    }

    //Mark completed queue token status
    public QueueToken completeToken(Long tokenId){

        Optional<QueueToken> tokenResult = queueTokenRepository.findById(tokenId);
        if(tokenResult.isEmpty()){
            throw new IllegalArgumentException("No queue token found with id: " + tokenId);
        }

        QueueToken token = tokenResult.get();
        if(token.getStatus() == TokenStatus.COMPLETED || token.getStatus() == TokenStatus.SKIPPED){
            throw new IllegalArgumentException("Cannot complete a token that is already: " + token.getStatus());
        }

        token.setStatus(TokenStatus.COMPLETED);
        return queueTokenRepository.save(token);
    }

    //Add this method here so that we can use in dashboard service
    public void calculateWaitTimesAndAlerts(List<QueueToken> waitingTokens, Department department) {
    for (int i = 0; i < waitingTokens.size(); i++) {
        QueueToken token = waitingTokens.get(i);
        int peopleAhead = i;
        double avgTimePerPatient = (department.getAvgTime() != null) ? department.getAvgTime() : 10.0;
        double estimatedWait = peopleAhead * avgTimePerPatient;
        token.setEstimatedWaitMinutes(estimatedWait);
        token.setShouldAlert(estimatedWait <= 15);
    }
}
   

}   

