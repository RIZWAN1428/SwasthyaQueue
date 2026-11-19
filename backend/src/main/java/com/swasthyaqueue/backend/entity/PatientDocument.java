package com.swasthyaqueue.backend.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
//every lookup of a patient’s documents will filter by hospital and patient.
//An index keeps that fast as the table grows.
@Table(name = "patient_documents", indexes = @Index(name = "idx_documents_hospital_patient",
                                columnList = "hospital_id, patient_id") )
public class PatientDocument {
    
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hospital_id", nullable = false)
    private Long hospitalId;

    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(name = "file_name", nullable = false, length = 255)
    private String fileName;

    @Column(name = "content_type", length = 100)
    private String contentType;

    @Column(name = "file_size", nullable = false)
    private Long fileSize;

    @Column(name = "file_data", nullable = false)
    //the file itself, as raw bytes. Hibernate stores it in a Postgres bytea column.
    private byte[] fileData;
    //a normal String column holds only 255 characters. 
    //OCR text can be pages long, and Postgres’s text type has no limit.
    @Column(name = "extracted_text", columnDefinition = "text")
    private String extractedText;

    @Column(name = "ocr_confidence")
    private Double ocrConfidence;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private DocumentStatus status;

    @Column(name = "uploaded_at", nullable = false)
    private LocalDateTime uploadedAt;

    public PatientDocument(){}

    public PatientDocument(Long hospitalId, Patient patient, String fileName, String contentType, byte[] fileData){
        this.hospitalId = hospitalId;
        this.patient = patient;
        this.fileName = fileName;
        this.contentType = contentType;
        this.fileSize = (long) fileData.length;
        this.fileData = fileData;   
        this.status = DocumentStatus.UPLOADED;
        this.uploadedAt = LocalDateTime.now();
    }

    public void markExtracted(String text, Double confidence){
        this.extractedText = text;
        this.ocrConfidence = confidence;
        this.status = DocumentStatus.EXTRACTED;
    }


    public void markFailed(){
        this.status = DocumentStatus.FAILED;
    }   

    public Long getId(){
        return id;
    }
    public Long getHospitalId(){
        return hospitalId;
    }
    public Patient getPatient(){
        return patient;
    }
    public String getFileName(){
        return fileName;
    }
    public String getContentType(){
        return contentType;
    }
    public Long getFileSize(){
        return fileSize;
    }
    public byte[] getFileData(){
        return fileData;
    }
    public String getExtractedText(){
        return extractedText;
    }
    public Double getOcrConfidence(){
        return ocrConfidence;
    }
    public DocumentStatus getStatus(){
        return status;
    }
    public LocalDateTime getUploadedAt(){
        return uploadedAt;
    }
}
