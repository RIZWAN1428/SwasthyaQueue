package com.swasthyaqueue.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.swasthyaqueue.backend.entity.Department;
import com.swasthyaqueue.backend.entity.Patient;
import com.swasthyaqueue.backend.entity.QueueToken;
import com.swasthyaqueue.backend.entity.TokenStatus;

public interface QueueTokenRepository extends JpaRepository<QueueToken, Long> {
    //There is race condition(like for one user 2 token can be created) because first read then write
    // so it got duplicated, to solving this we use sequence fo postgres to generate unique code.
    //nativeQuery = true - tells spring don't try to interpret as JPQL(mean in own language)
    @Query(value = "SELECT nextval('token_number_seq')", nativeQuery = true)
    Long getNextTokenSequeValue();
    //for below spring generate {SELECT * FROM queue_tokens WHERE department_id = ? AND status = ?}
    List<QueueToken> findByDepartmentAndStatus(Department department, TokenStatus status);

    List<QueueToken> findByDepartment(Department department);
    
    List<QueueToken> findByStatus(TokenStatus status);

    //does this queutoken exist for this exact patient at exactdpeartment with this exact status?
    boolean existsByPatientAndDepartmentAndStatus(Patient patient, Department department, TokenStatus status);
}