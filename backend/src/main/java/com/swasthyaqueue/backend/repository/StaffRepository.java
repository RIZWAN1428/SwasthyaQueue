package com.swasthyaqueue.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.swasthyaqueue.backend.entity.Staff;

public interface StaffRepository extends JpaRepository<Staff, Long> {
    
    boolean existsByUserName(String userName);
    Optional<Staff> findByUserName(String userName);
}
