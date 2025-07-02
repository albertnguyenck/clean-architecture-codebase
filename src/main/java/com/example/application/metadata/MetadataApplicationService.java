package com.example.application.metadata;

import com.example.application.metadata.usecase.GetAllMetadataUseCase;
import com.example.application.metadata.dto.MetadataDto;
import com.example.infrastructure.external.metadata.client.MetadataServiceClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MetadataApplicationService implements GetAllMetadataUseCase {
    private final MetadataServiceClient metadataServiceClient;

    public MetadataApplicationService(MetadataServiceClient metadataServiceClient) {
        this.metadataServiceClient = metadataServiceClient;
    }

    @Override
    public List<MetadataDto> execute() {
        return metadataServiceClient.getAllMetadata()
            .stream()
            .map(this::convertToDto)
            .toList();
    }
    
    private MetadataDto convertToDto(com.example.infrastructure.external.metadata.dto.response.MetadataResponse response) {
        return new MetadataDto(
            response.id(),
            response.type(),
            response.value(),
            response.createdAt()
        );
    }
} 