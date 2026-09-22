package com.swasthyaqueue.backend.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.swasthyaqueue.backend.dto.PatientRegistrationRequest;
import com.swasthyaqueue.backend.dto.PatientResponse;
import com.swasthyaqueue.backend.entity.Patient;
import com.swasthyaqueue.backend.service.PatientService;

//automatically convert whatever methods in this class and return into JSON.
@RestController 
@RequestMapping("/api/patients")
public class PatientController {
  
    @Autowired
    private PatientService patientService;

    //tells spring this method handles http POST request.
    @PostMapping 
    //@Requestbody tells srping that take raw json from request's body and automatically convert into
    //PatientRegistrationRequest object.
    public PatientResponse registerPatient(@RequestBody PatientRegistrationRequest request){
        
        Patient savedPatient = patientService.registerPatient(
            request.getFullName(),
            request.getDateOfBirth(),
            request.getPhoneNumber()
        );

        return new PatientResponse(
            savedPatient.getId(),
            savedPatient.getFullName(),
            savedPatient.getDateOfBirth(),
            savedPatient.getPhoneNumber(),
            savedPatient.getCreatedAt()
        );
    } 

    //Get patient by id
    @GetMapping("/{id}")
    public PatientResponse getPatientById(@PathVariable Long id){
        Patient patient = patientService.getPatientById(id);
        return toResponse(patient);
    }
    //Get all patient
    @GetMapping 
    public List<PatientResponse> getAllPatient(){

        List<Patient> patients = patientService.getAllPatient();
        return patients.stream().map(this::toResponse).collect(Collectors.toList());
    }

    private PatientResponse toResponse(Patient patient){
        return new PatientResponse(
            patient.getId(),
            patient.getFullName(),
            patient.getDateOfBirth(),
            patient.getPhoneNumber(),
            patient.getCreatedAt()
        );
    }

}
