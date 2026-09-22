package com.swasthyaqueue.backend.security;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component 
//OnceperRequestFilter :- A springbase class gauranting this filter runs exaclty once per request.
public class JwtAuthFilter extends OncePerRequestFilter{
    
    @Autowired 
    private JwtUtil jwtUtil;
    //doFilterInternal() already exists in Spring's OncePerRequestFilter in class.
    //class overrides it to add our own JWT filtering logic.
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                throws ServletException, IOException{
            //Get the authorization header from the incoming http request.
            String authHeader = request.getHeader("Authorization");
            //Check Request contains an authorizationheader start with bearer.
            if(authHeader != null && authHeader.startsWith("Bearer ")){
                //Remove bearer and give only jwt token...(why 7 because bearer_ are 7 characters.)
                String token = authHeader.substring(7);
                if(jwtUtil.validateToken(token)){
                String userName = jwtUtil.getUserNameFromToken(token);
                String role = jwtUtil.getRoleFromToken(token);
                //pass username, no password, role.
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userName, null, List.of(new SimpleGrantedAuthority("ROLE_" + role)));
                    //Stores authToken in Spring Security's context, so Spring now recognizes this request as authenticated.
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            //passes the request and response to next filter in chain.
            filterChain.doFilter(request, response);
    }

}
