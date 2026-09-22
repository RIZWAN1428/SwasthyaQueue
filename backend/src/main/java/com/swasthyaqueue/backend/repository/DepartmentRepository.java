package com.swasthyaqueue.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swasthyaqueue.backend.entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long>{
    
}
