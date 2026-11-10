package com.swasthyaqueue.backend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import com.swasthyaqueue.backend.entity.QueueToken;
import com.swasthyaqueue.backend.entity.TokenStatus;
import com.swasthyaqueue.backend.repository.QueueTokenRepository;
import java.util.ArrayList;
import java.util.List;
import com.swasthyaqueue.backend.entity.Department;
@ExtendWith(MockitoExtension.class)
class QueueTokenServiceTest {

    @Mock
    private QueueTokenRepository queueTokenRepository;

    @InjectMocks
    private QueueTokenService queueTokenService;

    @BeforeEach
    void loginAsHospitalOne() {
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken("staff1", null);
        auth.setDetails(1L);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @AfterEach
    void clearLogin() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void completeToken_waitingToken_becomesCompleted() {
        QueueToken token = new QueueToken(null, null, 3, "G-001", 1L);
        when(queueTokenRepository.findById(1L)).thenReturn(Optional.of(token));
        when(queueTokenRepository.save(any(QueueToken.class))).thenAnswer(inv -> inv.getArgument(0));

        QueueToken result = queueTokenService.completeToken(1L);

        assertEquals(TokenStatus.COMPLETED, result.getStatus());
        verify(queueTokenRepository).save(token);
    }

    @Test
    void completeToken_alreadyCompleted_throws() {
        QueueToken token = new QueueToken(null, null, 3, "G-001", 1L);
        token.setStatus(TokenStatus.COMPLETED);
        when(queueTokenRepository.findById(1L)).thenReturn(Optional.of(token));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> queueTokenService.completeToken(1L));

        assertTrue(ex.getMessage().contains("already"));
        verify(queueTokenRepository, never()).save(any(QueueToken.class));
    }

    @Test
    void completeToken_otherHospitalsToken_throws() {
        QueueToken token = new QueueToken(null, null, 3, "G-001", 2L);
        when(queueTokenRepository.findById(1L)).thenReturn(Optional.of(token));

        assertThrows(IllegalArgumentException.class,
                () -> queueTokenService.completeToken(1L));

        verify(queueTokenRepository, never()).save(any(QueueToken.class));
    }

    private List<QueueToken> waitingTokens(int count){
        List<QueueToken> tokens = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            tokens.add(new QueueToken(null, null, 3, "T-" + i, 1L));
        }
        return tokens;
    }

    @Test 
    void waitTimes_useDepartmentAvgTime_andAlertAtOrBelow15() {
        Department department = new Department("Consultation", 1L);
        department.setAvgTime(15.5);
        List<QueueToken> tokens = waitingTokens(3);

        queueTokenService.calculateWaitTimesAndAlerts(tokens, department);

        assertEquals(0.0, tokens.get(0).getEstimatedWaitMinutes(), 0.001);
        assertEquals(15.5, tokens.get(1).getEstimatedWaitMinutes(), 0.001);
        assertEquals(31.0, tokens.get(2).getEstimatedWaitMinutes(), 0.001);

        assertTrue(tokens.get(0).getShouldAlert());
        assertFalse(tokens.get(1).getShouldAlert());
        assertFalse(tokens.get(2).getShouldAlert());
    }

    @Test 
    void waitTimes_missingAvgTime_usesTenMinuteDefault() {
        Department department = new Department("Consultation", 1L);
        List<QueueToken> tokens = waitingTokens(3);

        queueTokenService.calculateWaitTimesAndAlerts(tokens, department);

        assertEquals(0.0, tokens.get(0).getEstimatedWaitMinutes(), 0.001);
        assertEquals(10.0, tokens.get(1).getEstimatedWaitMinutes(), 0.001);
        assertEquals(20.0, tokens.get(2).getEstimatedWaitMinutes(), 0.001);
    }

    @Test 
    void waitTimes_exactlyFifteenMinutes_stillAlerts() {
    Department department = new Department("Consultation", 1L);
    department.setAvgTime(15.0);
    List<QueueToken> tokens = waitingTokens(2);

    queueTokenService.calculateWaitTimesAndAlerts(tokens, department);

    assertEquals(15.0, tokens.get(1).getEstimatedWaitMinutes(), 0.001);
    assertTrue(tokens.get(1).getShouldAlert());
}
}