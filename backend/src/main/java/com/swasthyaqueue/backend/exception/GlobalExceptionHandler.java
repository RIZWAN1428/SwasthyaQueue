package com.swasthyaqueue.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

//Tells spring this class handles exceptions thrown by any controller in the application, centrally.
@RestControllerAdvice 
public class GlobalExceptionHandler {
    
    //whenever anywhere in application got illegalargumentexc,then run this method instead of500.
    @ExceptionHandler(IllegalArgumentException.class)
        //it controls the httpresponse
        //use conflict for 409.
        public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", ex.getMessage()));
        }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<Map<String, String>> handleOptimisticLock(ObjectOptimisticLockingFailureException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                              .body(Map.of("error", "This token was just updated by someone else. Please refresh and try again."));
    }
}
