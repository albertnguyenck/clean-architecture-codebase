package com.example.infrastructure.external.document.client;

import com.example.infrastructure.external.document.dto.response.DocumentResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
public class HttpDocumentServiceClient implements DocumentServiceClient {
    
    private final RestTemplate restTemplate;
    private final String baseUrl;
    
    public HttpDocumentServiceClient(
            RestTemplate restTemplate,
            @Value("${document.service.url:http://localhost:8081}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }
    
    @Override
    public List<DocumentResponse> getAllDocuments() {
        DocumentResponse[] documents = restTemplate.getForObject(
            baseUrl + "/api/documents", 
            DocumentResponse[].class
        );
        return documents != null ? Arrays.asList(documents) : List.of();
    }
    
    @Override
    public DocumentResponse getDocumentById(String id) {
        return restTemplate.getForObject(
            baseUrl + "/api/documents/" + id, 
            DocumentResponse.class
        );
    }
} 