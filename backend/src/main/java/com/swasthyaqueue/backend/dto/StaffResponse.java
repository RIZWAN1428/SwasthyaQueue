package com.swasthyaqueue.backend.dto;

import com.swasthyaqueue.backend.entity.Role;

public class StaffResponse {
    
    private Long id;
    private String userName;
    private Role role;

    public StaffResponse(Long id, String userName,Role role ){
        this.id = id;
        this.userName = userName;
        this.role = role;
    }

    public Long getId(){
        return id;
    }
    public String getUserName(){
        return userName;
    }
    public Role getRole(){
        return role;
    } 
}
