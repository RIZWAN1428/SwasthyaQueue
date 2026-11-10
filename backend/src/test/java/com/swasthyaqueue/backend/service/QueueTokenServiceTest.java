package com.swasthyaqueue.backend.service;

import static org.junit.jupiter.api.Assertions.*;
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
}