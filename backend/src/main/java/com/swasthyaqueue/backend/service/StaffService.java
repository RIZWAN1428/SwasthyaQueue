package com.swasthyaqueue.backend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.swasthyaqueue.backend.repository.StaffRepository;
import com.swasthyaqueue.backend.entity.Staff;

@Service 
public class StaffService {
    

    @Autowired 
    private StaffRepository staffRepository;

    //Inject passwordEncoder object.
    @Autowired 
    private PasswordEncoder passwordEncoder;

    //Register Staff
    public Staff regiserStaff(String userName, String password){

        boolean userNameExists = staffRepository.existsByUserName(userName);
        if(userNameExists){
            throw new IllegalArgumentException("A staff with this user name is already exist.");
        }
        //Hash the password before saving in repository.
        String hashedPassword = passwordEncoder.encode(password);
        Staff newStaff = new Staff(userName, hashedPassword);
        return staffRepository.save(newStaff);

    }

    //Login staff
    public Staff loginStaff(String userName, String password){
        
        Optional<Staff> staffResult = staffRepository.findByUserName(userName);
        if(staffResult.isEmpty()){
            throw new IllegalArgumentException("Invalid user name or password");
        }

        Staff staff = staffResult.get();
        boolean passwordMatches = passwordEncoder.matches(password, staff.getPasswordHash());
        if(!passwordMatches){
            throw new IllegalArgumentException("Invalid user name or password");
        }

        return staff;
    }
}
