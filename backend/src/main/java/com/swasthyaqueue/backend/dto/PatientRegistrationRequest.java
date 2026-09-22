package com.swasthyaqueue.backend.dto;

import java.time.LocalDate;

public class PatientRegistrationRequest {
    private String  fullName;
    private LocalDate dateOfBirth;
    private String phoneNumber;

    public String getFullName(){
        return fullName;
    }
    public void setFullName(String fullName){
        this.fullName  = fullName;
    }

    public LocalDate getDateOfBirth(){
        return dateOfBirth;
    }
    public void setDateOfBirth(LocalDate dateOfBirth){
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhoneNumber(){
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }

}
