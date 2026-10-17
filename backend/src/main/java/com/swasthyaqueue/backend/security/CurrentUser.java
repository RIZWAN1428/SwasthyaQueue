package com.swasthyaqueue.backend.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class CurrentUser {
    
    public static Long getHospitalId(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        //instanceof Long asks “is this thing really a Long".
        if(auth == null || !(auth.getDetails() instanceof Long)) {
            throw new  IllegalStateException("No hospital found for the current user.");
        }

        return (Long) auth.getDetails();
    }
}
