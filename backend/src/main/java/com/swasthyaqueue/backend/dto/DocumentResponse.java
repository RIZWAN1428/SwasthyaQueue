package com.swasthyaqueue.backend.dto;

import java.time.LocalDateTime;

import com.swasthyaqueue.backend.entity.PatientDocument;

public record DocumentResponse(
        Long id,
        Long patientId,
        String fileName,
        String contentType,
        Long fileSize,
        String status,
        Double ocrConfidence,
        String extractedText,
        LocalDateTime uploadedAt) {

    public static DocumentResponse from(PatientDocument d) {
        return new DocumentResponse(
                d.getId(),
                d.getPatient().getId(),
                d.getFileName(),
                d.getContentType(),
                d.getFileSize(),
                d.getStatus().name(),
                d.getOcrConfidence(),
                d.getExtractedText(),
                d.getUploadedAt());
    }
}