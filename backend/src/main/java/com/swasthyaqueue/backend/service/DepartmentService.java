package com.swasthyaqueue.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swasthyaqueue.backend.entity.Department;
import com.swasthyaqueue.backend.entity.DepartmentPrerequisite;

import com.swasthyaqueue.backend.repository.DepartmentRepository;
import com.swasthyaqueue.backend.security.CurrentUser;
import com.swasthyaqueue.backend.repository.DepartmentPrerequisiteRepository;

@Service 
public class DepartmentService {
    
    @Autowired 
    private DepartmentRepository departmentRepository;

    @Autowired 
    private DepartmentPrerequisiteRepository departmentPrerequisiteRepository;

    //Create a Department
    public Department createDepartment(String name){
        Department department = new Department(name, CurrentUser.getHospitalId());
        return departmentRepository.save(department);
    }
    //Get all department
    public List<Department> getAllDepartments(){
        return departmentRepository.findByHospitalId(CurrentUser.getHospitalId());
    }

    //Prerequisite Department 
    public DepartmentPrerequisite addPrerequisite(Long departmentId, Long prerequisiteDepartmentId){

        Department department = getDepartmentInMyHospital(departmentId);
        Department prerequisiteDepartment = getDepartmentInMyHospital(prerequisiteDepartmentId);

        if(departmentId.equals(prerequisiteDepartmentId)){
            throw new IllegalArgumentException("department can't be same with prerequisite department.");
        }

        boolean alreadyLinked = departmentPrerequisiteRepository.existsByDepartmentAndPrerequisiteDepartment(department,prerequisiteDepartment);
        if(alreadyLinked){
            throw new IllegalArgumentException("This Prerequisite is already linked.");
        }
        
        DepartmentPrerequisite prerequisite = new  DepartmentPrerequisite();
        prerequisite.setDepartment(department);
        prerequisite.setPrerequisiteDepartment(prerequisiteDepartment);

        return departmentPrerequisiteRepository.save(prerequisite);

    }

     //Update avgTime , like how much patient generally have to wait per department
    public Department updateAvgTime(Long id, Double avgTime){      
        Department department = getDepartmentInMyHospital(id);
        department.setAvgTime(avgTime);
        return departmentRepository.save(department);
        
    }

    private Department getDepartmentInMyHospital(Long id){
        Department department = departmentRepository.findById(id).orElseThrow(() ->
            new IllegalArgumentException("No department found with id: " + id));
        
         if (!department.getHospitalId().equals(CurrentUser.getHospitalId())) {
        throw new IllegalArgumentException("No department found with id: " + id);
    }
    return department;
    }
}
