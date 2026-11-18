package com.swasthyaqueue.backend.service;

import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.swasthyaqueue.backend.client.OcrClient;
import com.swasthyaqueue.backend.dto.OcrResult;
import com.swasthyaqueue.backend.entity.Patient;
import com.swasthyaqueue.backend.entity.PatientDocument;
import com.swasthyaqueue.backend.repository.PatientDocumentRepository;
import com.swasthyaqueue.backend.repository.PatientRepository;
import com.swasthyaqueue.backend.security.CurrentUser;

@Service 
public class DocumentService {

    private static final Logger log = LoggerFactory.getLogger(DocumentService.class);
    private static final long MAX_BYTES = 10L * 1024 * 1024 ;

    private static final List<String> ALLOWED = List.of(".pdf", ".png", ".jpg", ".jpeg");

    @Autowired 
    private PatientRepository patientRepository;

    @Autowired 
    private PatientDocumentRepository documentRepository;

    @Autowired 
    private OcrClient ocrClient;

    public PatientDocument upload(Long patientId, MultipartFile file){
        Long hosptitalId = CurrentUser.getHospitalId();

        Patient patient = patientRepository.findById(patientId)
                .filter(p -> p.getHospitalId().equals(hosptitalId))
                .orElseThrow(() -> new IllegalArgumentException("No patient found with id: " + patientId));
        
        String fileName = cleanFileName(file.getOriginalFilename());
        validate(file, fileName);

        byte[] data;
        try{
            data = file.getBytes();
        }catch(IOException e){
            throw new IllegalArgumentException("Could not read the uploaded file.");
        }
        //we save before ocr , so that if python service is down still it saves.
        PatientDocument document = documentRepository.save(
            new PatientDocument(hosptitalId, patient, fileName, file.getContentType(), data));

        try {
            OcrResult result = ocrClient.extract(fileName, data);
            document.markExtracted(joinPages(result), averageConfidence(result));
        } catch (RuntimeException e) {
            log.warn("OCR failed for document {}", document.getId(), e);
            document.markFailed();
        }

        return documentRepository.save(document);
    }

    private void validate(MultipartFile file, String fileName){
        if(file.isEmpty()){
            throw new IllegalArgumentException("The file is empty.");
        }

        if(file.getSize() > MAX_BYTES){
            throw new IllegalArgumentException("The file is larger than 10 MB.");
        }
        String lower = fileName.toLowerCase();
        if(ALLOWED.stream().noneMatch(lower::endsWith)){
            throw new IllegalArgumentException("Only PDF, PNG and JPG files are supported.");
        }
    }

    private String cleanFileName(String original){
        if(original == null || original.isBlank()){
            return "document";
        }
        String cleaned = original.replaceAll("[\\\\/]", "_").trim();
        return cleaned.length() > 255 ? cleaned.substring(0, 255) : cleaned;
    }

    private String joinPages(OcrResult result){
        StringBuilder text = new StringBuilder();

        for(OcrResult.Page page : result.pages()){
            if(!text.isEmpty()){
                text.append("\n\n");
            }
            text.append("[Page ").append(page.page()).append("]\n").append(page.text());
        }
        return text.toString();
    }

    private double averageConfidence(OcrResult  result){
        return result.pages().stream().mapToDouble(OcrResult.Page::confidence).average().orElse(0.0);
    
    }

}
