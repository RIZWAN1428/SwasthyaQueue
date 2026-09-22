package com.swasthyaqueue.backend.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity 
@Table(name = "departments")
public class Department {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;
    
    //Help Department to look back and know what are the prerequisites.
    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
    private List<DepartmentPrerequisite> prerequisites = new ArrayList<>();

    //average time for patient in per department
    @Column(name = "avg_time")
    private Double avgTime;
    public List<DepartmentPrerequisite> getPrerequisites(){
        return prerequisites;
    }

    public Department(){}

    public Department(String name){
        this.name =name;
    }
    public Long getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public Double getAvgTime(){
        return avgTime;
    }
    public void setAvgTime(Double avgTime){
        this.avgTime = avgTime;
    }

}
