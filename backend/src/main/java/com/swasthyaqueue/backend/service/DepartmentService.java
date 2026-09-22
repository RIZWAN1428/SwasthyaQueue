package com.swasthyaqueue.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swasthyaqueue.backend.entity.Department;
import com.swasthyaqueue.backend.entity.DepartmentPrerequisite;

import com.swasthyaqueue.backend.repository.DepartmentRepository;

import com.swasthyaqueue.backend.repository.DepartmentPrerequisiteRepository;

@Service 
public class DepartmentService {
    
    @Autowired 
    private DepartmentRepository departmentRepository;

    @Autowired 
    private DepartmentPrerequisiteRepository departmentPrerequisiteRepository;

    //Create a Department
    public Department createDepartment(String name){
        Department department = new Department(name);
        return departmentRepository.save(department);
    }
    //Get all department
    public List<Department> getAllDepartments(){
        return departmentRepository.findAll();
    }

    //Prerequisite Department 
    public DepartmentPrerequisite addPrerequisite(Long departmentId, Long prerequisiteDepartmentId){

        Optional<Department> departmentResult = departmentRepository.findById(departmentId);
        if(departmentResult.isEmpty()){
            throw new IllegalArgumentException("No department found with id: " +departmentId);
        }

        Optional<Department> prerequisiteResult = departmentRepository.findById(prerequisiteDepartmentId);
        if(prerequisiteResult.isEmpty()){
            throw new IllegalArgumentException("No department found with id: " +prerequisiteDepartmentId);
        }

        if(departmentId.equals(prerequisiteDepartmentId)){
            throw new IllegalArgumentException("department can't be same with prerequisite department.");
        }

        boolean alreadyLinked = departmentPrerequisiteRepository.existsByDepartmentAndPrerequisiteDepartment(departmentResult.get(),prerequisiteResult.get());
        if(alreadyLinked){
            throw new IllegalArgumentException("This Prerequisite is already linked.");
        }
        
        DepartmentPrerequisite prerequisite = new  DepartmentPrerequisite();
        prerequisite.setDepartment(departmentResult.get());
        prerequisite.setPrerequisiteDepartment(prerequisiteResult.get());

        return departmentPrerequisiteRepository.save(prerequisite);

    }

     //Update avgTime , like how much patient generally have to wait per department
    public Department updateAvgTime(Long id, Double avgTime){

        Optional<Department> avgTimeResult = departmentRepository.findById(id);
        if(avgTimeResult.isEmpty()){
            throw new IllegalArgumentException("No department found with this ID: " + id);
        }
       
        Department department = avgTimeResult.get();
        department.setAvgTime(avgTime);
        return departmentRepository.save(department);
        
    }


}
