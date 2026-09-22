package com.swasthyaqueue.backend.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


import com.swasthyaqueue.backend.security.JwtAuthFilter;

@Configuration 
public class SecurityConfig {
    
    @Bean 
    //PasswordEncoder - Interface
    //BCryptPasswordEncoder - Specific implemtation of that passwordEncoder
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }   

    @Autowired 
    private JwtAuthFilter jwtAuthFilter;

    //CSRF disable
    //permit all route from security config to make it enable
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/staff/login").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/queue-tokens/department/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/departments").permitAll()
                .requestMatchers("/api/departments/**").hasRole("ADMIN")
                .requestMatchers("/api/staff/**").hasRole("ADMIN")
                .anyRequest().hasRole("STAFF")
            )
            //tells spring to run our customjwt auth filter before its own built in authentication filter.
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    //cors configuration
    @Bean 
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT",  "DELETE"));
        configuration.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        //applies this rule to every endpoint (/** matches all paths).
        source.registerCorsConfiguration("/**", configuration);
        return source;

    }

   
}
