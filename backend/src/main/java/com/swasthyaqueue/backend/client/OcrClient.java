package com.swasthyaqueue.backend.client;

import java.time.Duration;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import com.swasthyaqueue.backend.dto.OcrResult;

import org.springframework.beans.factory.annotation.Value;

@Component 
public class OcrClient {

       // Spring’s tool for calling another web service.
        private final RestClient restClient;

        public OcrClient(@Value("${ocr.service.url}") String baseUrl){
            JdkClientHttpRequestFactory factory = new JdkClientHttpRequestFactory();
            //without a timeout, a stuck OCR service would hold one of your request threads forever.
            factory.setReadTimeout(Duration.ofSeconds(60));

            this.restClient = RestClient.builder()
                    .baseUrl(baseUrl)
                    .requestFactory(factory)
                    .build();

        }

        public OcrResult extract(String fileName, byte[] data){
            //an upload must have a file name, or the Python side treats it as an ordinary form field and not a file
            ByteArrayResource resource = new ByteArrayResource(data){
                @Override 
                public String getFilename(){
                    return  fileName;
                }
            };

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", resource);
            //.retrieve().body(OcrResult.class): send the request, then convert the JSON reply into our record.
            try{
                return restClient.post()
                        .uri("/extract")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .body(body)
                        .retrieve()
                        .body(OcrResult.class);
            //if the Python service is stopped, we throw one clear message instead of a long network error.
            }catch(RestClientException e){
                    throw new IllegalStateException("The document reader is not available right now.", e);
            }
        }
}
