package com.swasthyaqueue.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swasthyaqueue.backend.entity.PatientDocument;

public interface PatientDocumentRepository extends JpaRepository<PatientDocument, Long>{
    
} 
