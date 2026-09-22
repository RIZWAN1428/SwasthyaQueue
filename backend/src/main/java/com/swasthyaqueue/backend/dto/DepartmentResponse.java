package com.swasthyaqueue.backend.dto;


public class DepartmentResponse {
    
    private Long id;
    private String name;
    private Double avgTime;
    
    public DepartmentResponse(Long id, String name, Double avgTime ){
        this.id = id;
        this.name = name;
        this.avgTime = avgTime;
    }

    public Long getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public Double getAvgTime(){
        return avgTime;
    }

}
