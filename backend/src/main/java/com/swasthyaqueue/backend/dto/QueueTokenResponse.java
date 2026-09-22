package com.swasthyaqueue.backend.dto;

import com.swasthyaqueue.backend.entity.TokenStatus;
import java.time.LocalDateTime;

public class QueueTokenResponse {

    private Long id;
    private String tokenNumber;
    private Long patientId;
    private String patientName;
    private Long departmentId;
    private String departmentName;
    private Integer severityScore;
    private Double priorityScore;
    private TokenStatus status;
    private LocalDateTime arrivalTimestamp;
    private Double estimatedWaitMinutes;
    private Boolean shouldAlert;

    public QueueTokenResponse(Long id, String tokenNumber, Long patientId, String patientName,
                               Long departmentId, String departmentName, Integer severityScore,
                               Double priorityScore, TokenStatus status, LocalDateTime arrivalTimestamp,
                                Double estimatedWaitMinutes, Boolean shouldAlert) {
        this.id = id;
        this.tokenNumber = tokenNumber;
        this.patientId = patientId;
        this.patientName = patientName;
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.severityScore = severityScore;
        this.priorityScore = priorityScore;
        this.status = status;
        this.arrivalTimestamp = arrivalTimestamp;
        this.estimatedWaitMinutes = estimatedWaitMinutes;
        this.shouldAlert = shouldAlert;
    }

    public Long getId() { return id; }
    public String getTokenNumber() { return tokenNumber; }
    public Long getPatientId() { return patientId; }
    public String getPatientName() { return patientName; }
    public Long getDepartmentId() { return departmentId; }
    public String getDepartmentName() { return departmentName; }
    public Integer getSeverityScore() { return severityScore; }
    public Double getPriorityScore() { return priorityScore; }
    public TokenStatus getStatus() { return status; }
    public LocalDateTime getArrivalTimestamp() { return arrivalTimestamp; }
    public Double getEstimatedWaitMinutes() {return estimatedWaitMinutes;}  
    public Boolean getShouldAlert(){return shouldAlert;}
}