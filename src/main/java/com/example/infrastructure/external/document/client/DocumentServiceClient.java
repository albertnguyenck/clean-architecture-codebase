package com.example.infrastructure.external.document.client;

import com.example.infrastructure.external.document.dto.response.DocumentResponse;

import java.util.List;

public interface DocumentServiceClient {
    List<DocumentResponse> getAllDocuments();
    DocumentResponse getDocumentById(String id);
} 