package com.swasthyaqueue.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.swasthyaqueue.backend.dto.DashboardResponse;
import com.swasthyaqueue.backend.service.DashboardService;

@RestController 
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired 
    private DashboardService dashboardService;

    @GetMapping
    public DashboardResponse getDashboardSummary(){
        DashboardResponse dashboardSummary = dashboardService.getDashboardSummary();
        return dashboardSummary;
    }
    
}
