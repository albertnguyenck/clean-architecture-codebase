package com.example.infrastructure.external.metadata.client;

import com.example.infrastructure.external.metadata.dto.response.MetadataResponse;

import java.util.List;

public interface MetadataServiceClient {
    List<MetadataResponse> getAllMetadata();
    MetadataResponse getMetadataById(String id);
} 