package com.swasthyaqueue.backend.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swasthyaqueue.backend.entity.Patient;
import com.swasthyaqueue.backend.repository.PatientRepository;

@Service //tells this class contains business logic, manage it as a Bean.
public class PatientService {
    
    @Autowired //Instead of PatientService creating patientRepo maually, spring gives us workingone at a startup.
    //No need to create like new PatientRepo() etc. , its called Dependency Injection.
    private PatientRepository patientRepository; 

    //Register patient
    public Patient registerPatient(String fullName, LocalDate dateOfBirth, String phoneNumber){
        boolean phoneExists = patientRepository.existsByPhoneNumber(phoneNumber);

        if(phoneExists){
            throw new IllegalArgumentException("A patient with this phone number is already registered.");
        }
        Patient newPatient = new Patient(fullName, dateOfBirth, phoneNumber);
        return patientRepository.save(newPatient);
    }
    //Get patient by id 
    public Patient getPatientById(Long id){
        //Optional - might have a patient side or empty, can handle both cases.
        //findById - we get this from JpaRepository
        Optional<Patient> result = patientRepository.findById(id);

        if(result.isEmpty()){
            throw new IllegalArgumentException("No patient found with id: " +id);
        }

        return result.get();
    }
    //Get All patient
    public List<Patient> getAllPatient(){
        return patientRepository.findAll();        
    }
}
