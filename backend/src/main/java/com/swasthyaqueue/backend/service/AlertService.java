package com.swasthyaqueue.backend.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service 
public class AlertService {

    @Async
    public void sendAlert(Long hospitalId, String tokenNumber, String departmentName){
        System.out.println( "Alert started on thread: "+ Thread.currentThread().getName());

        try{
            Thread.sleep(3000); //pretend to be a slow sms calls
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }

        System.out.println("ALERT sent -> hospital " + hospitalId + ", token " + tokenNumber 
            + " (" + departmentName + ") is almost up"
        );
    }
}