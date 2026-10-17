package com.swasthyaqueue.backend.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swasthyaqueue.backend.entity.Patient;
import com.swasthyaqueue.backend.repository.PatientRepository;
import com.swasthyaqueue.backend.security.CurrentUser;

@Service //tells this class contains business logic, manage it as a Bean.
public class PatientService {
    
    @Autowired //Instead of PatientService creating patientRepo maually, spring gives us workingone at a startup.
    //No need to create like new PatientRepo() etc. , its called Dependency Injection.
    private PatientRepository patientRepository; 

    //Register patient
    public Patient registerPatient(String fullName, LocalDate dateOfBirth, String phoneNumber){
        
        Long hospitalId = CurrentUser.getHospitalId();

        if(patientRepository.existsByPhoneNumberAndHospitalId(phoneNumber, hospitalId)){
            throw new IllegalArgumentException("A patient with this phone number is already registered.");
        }
        Patient newPatient = new Patient(fullName, dateOfBirth, phoneNumber, hospitalId);
        return patientRepository.save(newPatient);
    }
    //Get patient by id 
    public Patient getPatientById(Long id){
        //Optional - might have a patient side or empty, can handle both cases.
        //findById - we get this from JpaRepository
        Long hospitalId = CurrentUser.getHospitalId();

        return patientRepository.findById(id)
            .filter(p -> p.getHospitalId().equals(hospitalId))
            .orElseThrow(() -> new IllegalArgumentException("No patient found with id: " + id));

    }
    //Get All patient
    public List<Patient> getAllPatient(){
        return patientRepository.findByHospitalId(CurrentUser.getHospitalId());       
    }
}
