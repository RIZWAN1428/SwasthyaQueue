package com.swasthyaqueue.backend.dto;

public class DepartmentPrerequisiteResponse {
    private Long id;
    private String departmentName;
    private Long departmentId;
    private String prerequisiteDepartmentName;
    private Long prerequisiteDepartmentId;

    //Constructor:- how does the value get into the field in the first place
    public DepartmentPrerequisiteResponse(Long id, String departmentName, Long departmentId, String prerequisiteDepartmentName, Long prerequisiteDepartmentId){
            this.id = id;
            this.departmentName = departmentName;
            this.departmentId = departmentId;
            this.prerequisiteDepartmentId = prerequisiteDepartmentId;
            this.prerequisiteDepartmentName = prerequisiteDepartmentName;
    }
    //get :-  How do i read the value  
    public Long getId(){
        return id;
    }
    public String getDepartmentName(){
        return departmentName;
    }
    public Long getDepartmentId(){
        return departmentId;
    }
    public String getPrerequisiteDepartmentName(){
        return prerequisiteDepartmentName;
    }
    public Long getPrerequisiteDepartmentId(){
        return prerequisiteDepartmentId;
    }
}
