package com.swasthyaqueue.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swasthyaqueue.backend.entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long>{
    
    List<Department> findByHospitalId(Long hospitalId);
}
