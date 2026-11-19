package com.swasthyaqueue.backend.controller;

import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.swasthyaqueue.backend.dto.DocumentResponse;
import com.swasthyaqueue.backend.service.DocumentService;

@RestController 
//A document belongs to a patient, so the URL says so: “documents of patient 5”.
@RequestMapping("/api/patients/{patientId}/documents")
public class DocumentController {
    
    @Autowired 
    private DocumentService documentService;
    //this endpoint accepts only file-upload requests, not JSON.
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public DocumentResponse upload(@PathVariable Long patientId, 
                    @RequestParam("file") MultipartFile file){

        return DocumentResponse.from(documentService.upload(patientId, file));
    }

    


    
}
