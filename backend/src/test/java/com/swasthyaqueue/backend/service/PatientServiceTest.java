package com.swasthyaqueue.backend.service;


import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import com.swasthyaqueue.backend.entity.Patient;
import com.swasthyaqueue.backend.repository.PatientRepository;

@ExtendWith(MockitoExtension.class)//turns on Mockito’s fakes for this class.
class PatientServiceTest {

    @Mock//creates a fake PatientRepository that never touches a database.
    private PatientRepository patientRepository;

    @InjectMocks //creates a real PatientService and plugs the fake in, like @Autowired would.
    private PatientService patientService;

    @BeforeEach//run before the test
    void loginAsHospitalOne() {
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken("staff1", null);
        auth.setDetails(1L);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @AfterEach//run after the test
    void clearLogin() {
        SecurityContextHolder.clearContext();
    }
    //marks a method as a test case that Maven runs.
    @Test
    void registerPatient_duplicatePhone_throws() {
        //tells the fake “if asked whether this phone exists in hospital 1, answer yes.”
        when(patientRepository.existsByPhoneNumberAndHospitalId("9999999999", 1L)).thenReturn(true);

        //the test passes only if the code throws that exception
        assertThrows(IllegalArgumentException.class, () ->
                patientService.registerPatient("Test", LocalDate.of(2000, 1, 1), "9999999999"));

        //also proves nothing was saved.
        verify(patientRepository, never()).save(any(Patient.class));
    }
}

