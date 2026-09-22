package com.swasthyaqueue.backend.dto;

public class TokenCreateRequest {
    
    private Long patientId;
    private Long departmentId;
    private Integer severityScore;

    public Long getPatientId(){
        return patientId;
    }
    public void setPatientId(Long patientId){
        this.patientId = patientId;
    }
    public Long getDepartmentId(){
        return departmentId;
    }
    public void setDepartmentId(Long departmentId){
        this.departmentId = departmentId;
    }
    public Integer getSeverityScore(){
        return severityScore;
    }
    public void setSeverityScore(Integer severityScore){
        this.severityScore = severityScore;
    }
}
