package com.swasthyaqueue.backend.security;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

public class CurrentUser {
    
    public static Long getHospitalId(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        //instanceof Long asks “is this thing really a Long".
        if(auth == null || !(auth.getDetails() instanceof Long)) {
            throw new  IllegalStateException("No hospital found for the current user.");
        }

        return (Long) auth.getDetails();
    }
    //Check if staff logged in or public accessing.
    public static boolean isLoggedIn(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken);
    }
}
