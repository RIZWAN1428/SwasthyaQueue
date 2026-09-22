package com.swasthyaqueue.backend.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.swasthyaqueue.backend.dto.QueueTokenResponse;
import com.swasthyaqueue.backend.dto.TokenCreateRequest;
import com.swasthyaqueue.backend.entity.QueueToken;
import com.swasthyaqueue.backend.service.QueueTokenService;

@RestController 
@RequestMapping("/api/queue-tokens")
public class QueueTokenController {
    
    @Autowired
    private QueueTokenService queueTokenService;

    //Create Token
    @PostMapping 
    public QueueTokenResponse createToken(@RequestBody TokenCreateRequest request){
        QueueToken token = queueTokenService.createToken(
            request.getPatientId(),
            request.getDepartmentId(),
            request.getSeverityScore()
        );

        return toResponse(token);
    }
    //get Queue for department
    @GetMapping("/department/{departmentId}")
    //We use path variable to map with department id to fetch specific resource.
    public List<QueueTokenResponse> getQueueForDepartment(@PathVariable Long departmentId){
       List<QueueToken> tokens = queueTokenService.getQueueForDepartment(departmentId);
       //Take the List<QueueToken>, map each individual QueueToken into a queuetokenresponse using helper
       //method then collect back into List<QueueTokenResponse>
       //this::toResponse ==> {token ->this.toResponse(token)}
       return tokens.stream().map(this::toResponse).collect(Collectors.toList());
    }

    //Get token for department
    @GetMapping("/department/{departmentId}/all")
    public List<QueueTokenResponse> getTokenforDepartment(@PathVariable Long departmentId){

        List <QueueToken> tokens = queueTokenService.getAllTokensForDepartment(departmentId);
        return tokens.stream().map(this::toResponse).collect(Collectors.toList());
    }

    //Private helper method to extract a method.
    private QueueTokenResponse toResponse(QueueToken token) {
        return new QueueTokenResponse(
            token.getId(),
            token.getTokenNumber(),
            token.getPatient().getId(),
            token.getPatient().getFullName(),
            token.getDepartment().getId(),
            token.getDepartment().getName(),
            token.getSeverityScore(),
            token.getPriorityScore(),
            token.getStatus(),
            token.getArrivalTimestamp(),
            token.getEstimatedWaitMinutes(),
            token.getShouldAlert()
        );
    }

    //update a queue token
    @PutMapping("/token/{tokenId}")

    public QueueTokenResponse updateToken(@PathVariable Long tokenId){

        QueueToken savedToken = queueTokenService.completeToken(tokenId);
        return toResponse(savedToken);
    }

}
