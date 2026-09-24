package com.swasthyaqueue.backend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swasthyaqueue.backend.dto.DashboardResponse;
import com.swasthyaqueue.backend.entity.Department;
import com.swasthyaqueue.backend.entity.QueueToken;
import com.swasthyaqueue.backend.entity.TokenStatus;
import com.swasthyaqueue.backend.repository.DepartmentRepository;
import com.swasthyaqueue.backend.repository.QueueTokenRepository;

@Service 
public class DashboardService {

    @Autowired
    private QueueTokenService queueTokenService;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private QueueTokenRepository queueTokenRepository;

    public DashboardResponse getDashboardSummary(){
        List<Department> allDepartments = departmentRepository.findAll();
        int totalDepartments = allDepartments.size();

        int totalPatientsWaiting = 0;
        List<String> departmentsWithAlerts = new ArrayList<>();

        for (Department department : allDepartments) {
        List<QueueToken> waitingTokens = queueTokenRepository.findByDepartmentAndStatus(department, TokenStatus.WAITING);
        totalPatientsWaiting += waitingTokens.size();

        queueTokenService.calculateWaitTimesAndAlerts(waitingTokens, department);

        boolean hasAlert = waitingTokens.stream().anyMatch(token -> token.getShouldAlert());

        if (hasAlert) {
            departmentsWithAlerts.add(department.getName());
        }
    }

    return new DashboardResponse(totalDepartments, totalPatientsWaiting, departmentsWithAlerts);

    }
} 