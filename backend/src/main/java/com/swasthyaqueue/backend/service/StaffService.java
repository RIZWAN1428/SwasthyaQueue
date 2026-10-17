package com.swasthyaqueue.backend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.swasthyaqueue.backend.repository.StaffRepository;
import com.swasthyaqueue.backend.security.CurrentUser;
import com.swasthyaqueue.backend.entity.Staff;

@Service 
public class StaffService {
    

    @Autowired 
    private StaffRepository staffRepository;

    //Inject passwordEncoder object.
    @Autowired 
    private PasswordEncoder passwordEncoder;

    //Register Staff
    public Staff registerStaff(String userName, String password){

        if (staffRepository.existsByUserName(userName)) {
        throw new IllegalArgumentException("A staff with this user name is already exist.");
    }

        //Hash the password before saving in repository.
        String hashedPassword = passwordEncoder.encode(password);
        Staff newStaff = new Staff(userName, hashedPassword, CurrentUser.getHospitalId());
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
