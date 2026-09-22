package com.swasthyaqueue.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity 
@Table(name = "staff")
public class Staff {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name", unique = true, nullable = false, length = 20)
    private String userName;

    @Column(name = "password_hash",nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    private Role role;
    //Hibernate (JPA) requires it. When Hibernate loads data from the database it needs to construct a blank Staff object first.
    public Staff(){}

    public Staff(String userName, String passwordHash){
        this.userName = userName;
        this.passwordHash = passwordHash;
        this.role = Role.STAFF;
    }

    public Long getId(){
        return id;
    }
    public String getUserName(){
        return userName;
    }
    public void setUserName(String userName){
        this.userName = userName;
    }
    public String getPasswordHash(){
        return passwordHash;
    }
    public void setPasswordHash(String passwordHash){
        this.passwordHash = passwordHash;
    }
    public Role getRole(){
        return role;
    }
    public void setRole(Role role){
        this.role = role; 
    }
}
