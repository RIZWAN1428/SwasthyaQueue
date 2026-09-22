package com.swasthyaqueue.backend.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.swasthyaqueue.backend.dto.DepartmentPrerequisiteResponse;
import com.swasthyaqueue.backend.dto.DepartmentResponse;

import com.swasthyaqueue.backend.entity.Department;
import com.swasthyaqueue.backend.entity.DepartmentPrerequisite;

import com.swasthyaqueue.backend.service.DepartmentService;

@RestController 
@RequestMapping("/api/departments")
public class DepartmentController {
  
    @Autowired 
    private DepartmentService departmentService;

    @PostMapping
    public DepartmentResponse createDepartment(@RequestBody Map<String, String> request){
        Department department = departmentService.createDepartment(request.get("name"));
        return toResponse(department);
    }

    @GetMapping 
    public List<DepartmentResponse> getAllDepartments(){
       List<Department> departments = departmentService.getAllDepartments();
       return departments.stream().map(this::toResponse).collect(Collectors.toList());
    }
    
    //Link Department to prerequisitedepartment
    @PostMapping("/{departmentId}/prerequisites/{prerequisiteDepartmentId}")
    public DepartmentPrerequisiteResponse addPrerequisite(
            @PathVariable Long departmentId,
            @PathVariable Long prerequisiteDepartmentId){
            
        DepartmentPrerequisite savedPrerequisite = departmentService.addPrerequisite(departmentId, prerequisiteDepartmentId);
        return toResponse(savedPrerequisite);
    }
    private DepartmentPrerequisiteResponse toResponse(DepartmentPrerequisite prerequisite) {
        return new DepartmentPrerequisiteResponse(
            prerequisite.getId(),
            prerequisite.getDepartment().getName(),
            prerequisite.getDepartment().getId(),
            prerequisite.getPrerequisiteDepartment().getName(),
            prerequisite.getPrerequisiteDepartment().getId()
        );
    }

    //update the avg time for per department
    @PutMapping("/{id}/avg-time")

    public DepartmentResponse setAvgTime(@PathVariable Long id, @RequestBody Double avgTime){

        Department savedTime = departmentService.updateAvgTime(id, avgTime);
        return toResponse(savedTime);
    }

    //Private helper method to extract a method.
    private DepartmentResponse toResponse(Department department) {
        return new DepartmentResponse(
            department.getId(),
            department.getName(),
            department.getAvgTime()    
        );
    }
}   
