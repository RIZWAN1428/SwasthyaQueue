package com.swasthyaqueue.backend.entity;

import jakarta.persistence.*; 

@Entity 
@Table(name = "department_prerequisites")
public class DepartmentPrerequisite {
    
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prerequisite_department_id", nullable = false)
    private Department prerequisiteDepartment;

    public Long getId(){
        return id;
    }
    public Department getDepartment(){
        return department;
    }
    public Department getPrerequisiteDepartment(){
        return prerequisiteDepartment;
    }

    public void setDepartment(Department department){
        this.department = department;
    }
    public void setPrerequisiteDepartment(Department prerequisiteDepartment){
        this.prerequisiteDepartment = prerequisiteDepartment;
    }
}
