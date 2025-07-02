package com.example.application.document;

import com.example.application.document.dto.DocumentDto;
import com.example.application.document.usecase.GetAllDocumentsUseCase;
import com.example.infrastructure.external.document.client.DocumentServiceClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentApplicationService implements GetAllDocumentsUseCase {
    
    private final DocumentServiceClient documentServiceClient;
    
    public DocumentApplicationService(DocumentServiceClient documentServiceClient) {
        this.documentServiceClient = documentServiceClient;
    }
    
    @Override
    public List<DocumentDto> execute() {
        return documentServiceClient.getAllDocuments()
            .stream()
            .map(this::convertToDto)
            .toList();
    }
    
    private DocumentDto convertToDto(com.example.infrastructure.external.document.dto.response.DocumentResponse response) {
        return new DocumentDto(
            response.id(),
            response.title(),
            response.content(),
            response.author(),
            response.createdAt()
        );
    }
} 