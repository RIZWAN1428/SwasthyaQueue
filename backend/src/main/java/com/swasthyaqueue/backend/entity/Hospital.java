package com.swasthyaqueue.backend.entity;

import jakarta.persistence.*;

@Entity 
@Table(name = "hospitals")
public class Hospital {
    

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "city", length = 100)
    private String city;

    public Hospital(){}

    public Hospital(String name, String city){
        this.name = name;
        this.city = city;
    }

    public Long getId(){return id;}
    public String getName(){return name; }
    public void setName(String name) {this.name = name;}
    public String getCity(){return city; }
    public void setCity(String city) {this.city = city;}

}
