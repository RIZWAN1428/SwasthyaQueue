package com.swasthyaqueue.backend.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.Table;

@Entity 
@Table(name = "queue_tokens")
public class QueueToken {
    
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Connect to patient table
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    //Connect to department table
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @Column(name = "severity_score", nullable = false)
    //int can not be null so we use Integer which allowed null (meaning "no severity assigned yet")
    private Integer severityScore; 

    @Column(name = "arrival_timestamp", nullable = false)
    private LocalDateTime arrivalTimestamp;

    //Queue token's status {we used enums for fixed values}
    @Enumerated(EnumType.STRING)//Store value as string not number which is ORDINAL
    @Column(name = "status", nullable = false, length = 20)
    private TokenStatus status;

    @Column(name = "token_number", nullable = false, unique = true, length = 20)
    private String tokenNumber;

    @Column(name = "priority_score")
    private Double  priorityScore;

    @Column(name = "estimated_wait_minutes")
    private Double estimatedWaitMinutes;

    @Column(name = "should_alert")
    private Boolean shouldAlert;

    public QueueToken(){}

    public QueueToken(Patient patient, Department department, Integer severityScore, String tokenNumber){
        this.patient = patient;
        this.department = department;
        this.severityScore = severityScore;
        this.tokenNumber = tokenNumber;
        this.arrivalTimestamp = LocalDateTime.now();
        this.status = TokenStatus.WAITING;
    }
    public Long getId(){
        return id;
    }
    public Patient getPatient(){
        return patient;
    }
    public Department getDepartment(){
        return department;
    }
    public Integer getSeverityScore(){
        return severityScore;
    }
    public void setSeverityScore(Integer severityScore){
        this.severityScore = severityScore;
    }
    public LocalDateTime getArrivalTimestamp(){
        return arrivalTimestamp;
    }
    public TokenStatus getStatus(){
        return status;
    }
    public void setStatus(TokenStatus status){
        this.status = status;
    }
    public String getTokenNumber(){
        return tokenNumber;
    }
    public Double getPriorityScore(){
        return priorityScore;
    }
    public void setPriorityScore(Double priorityScore){
        this.priorityScore = priorityScore;
    }

    public Double getEstimatedWaitMinutes(){
        return estimatedWaitMinutes;
    }
    public void setEstimatedWaitMinutes(Double estimatedWaitMinutes){
        this.estimatedWaitMinutes = estimatedWaitMinutes;
    }

    public Boolean getShouldAlert(){
        return shouldAlert;
    }
    public void setShouldAlert(Boolean shouldAlert){
        this.shouldAlert = shouldAlert;
    }
}
