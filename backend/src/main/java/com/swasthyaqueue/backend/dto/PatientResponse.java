package com.swasthyaqueue.backend.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PatientResponse {
    
    private Long id;
    private String fullName;
    private LocalDate dateOfBirth;
    private String phoneNumber;
    private LocalDateTime createdAt;

    public PatientResponse(Long id, String fullName, LocalDate dateOfBirth, String phoneNumber, LocalDateTime createdAt){
        this.id = id;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.createdAt = createdAt;
    }

    public Long getId(){
        return id;
    }
    public String getFullName(){
        return fullName;
    }
    public LocalDate getDateOfBirth(){
        return dateOfBirth;
    }
    public String getPhoneNumber(){
        return phoneNumber;
    }
    public LocalDateTime getCreatedAt(){
        return createdAt;
    }

}
