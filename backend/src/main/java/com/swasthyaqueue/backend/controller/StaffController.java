package com.swasthyaqueue.backend.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.swasthyaqueue.backend.service.*;
import com.swasthyaqueue.backend.entity.*;
import com.swasthyaqueue.backend.dto.*;
import com.swasthyaqueue.backend.security.*;


@RestController 
@RequestMapping("/api/staff")
public class StaffController {
    
    @Autowired
    public StaffService staffService;

    @Autowired 
    private JwtUtil jwtUtil;

    @PostMapping
    public StaffResponse createStaff(@RequestBody Map<String, String> request){
        Staff staff = staffService.regiserStaff(request.get("userName"), request.get("password"));
        return toResponse(staff);
    }

    private StaffResponse toResponse(Staff staff){
        return new StaffResponse(
            staff.getId(),
            staff.getUserName(),
            staff.getRole()
        );
    }

    //login staff
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> request){

        Staff staff = staffService.loginStaff(request.get("userName"), request.get("password"));
        String token = jwtUtil.generateToken(staff.getUserName(), staff.getRole().toString());

        Map<String, String> response = new HashMap<>();
        response.put("token", token);

        return response;
    }
}
