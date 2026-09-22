package com.swasthyaqueue.backend.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

//Map Objects(class with fields) to Tables in database
@Entity
@Table(name = "patients")
public class Patient {

    //primary key
    @Id 
    //Increament value automatically
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //uses full_name coz sql use snake_case but java use camelCase
    //nullable = false means this column can never be empty(NOT NULL Constraint)
    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;
    @Column(name = "phone_number", nullable = false, length = 15, unique = true)
    private String phoneNumber;
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    //For patient to know about its tokens
    //Cascade:- if we do a operation on parent, the child will affect too. Like delete the patient will delete queue tokens too.
    //LAzy :- delays loading related database until your code actually assess it.
    @OneToMany(mappedBy = "patient" , cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<QueueToken> queueTokens = new ArrayList<>();
    //no-arg constructor required by JPA
    public Patient(){      
    }
    //Convenience constructor when code creates a patient
    public Patient(String fullName, LocalDate dateOfBirth, String phoneNumber ){
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.createdAt = LocalDateTime.now();
    }

    //getters and setters
    public Long getId(){
        return id;
    }
    public String getFullName(){
        return fullName;
    }
    public void setFullName(String fullName){
        this.fullName = fullName;
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
    public LocalDateTime getCreatedAt(){
        return createdAt;
    }
    public List<QueueToken> getQueueTokens(){
        return queueTokens;
    }
}
