package com.swasthyaqueue.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swasthyaqueue.backend.entity.Patient;

//Patient - entity that repository manage, Long - primary key of entity
public interface PatientRepository extends JpaRepository<Patient, Long>{

    //existsBy - generate the query that check existence(return true/false)
    //phoneNumber - Tell spring which fields to check.
    boolean existsByPhoneNumber(String phoneNumber);
} 
