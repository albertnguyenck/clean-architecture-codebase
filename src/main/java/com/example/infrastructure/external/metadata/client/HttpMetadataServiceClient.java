package com.example.infrastructure.external.metadata.client;

import com.example.infrastructure.external.metadata.dto.response.MetadataResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
public class HttpMetadataServiceClient implements MetadataServiceClient {
    private final RestTemplate restTemplate;
    private final String baseUrl;

    public HttpMetadataServiceClient(
            RestTemplate restTemplate,
            @Value("${metadata.service.url:http://localhost:8082}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    @Override
    public List<MetadataResponse> getAllMetadata() {
        MetadataResponse[] metadata = restTemplate.getForObject(
            baseUrl + "/api/metadata",
            MetadataResponse[].class
        );
        return metadata != null ? Arrays.asList(metadata) : List.of();
    }

    @Override
    public MetadataResponse getMetadataById(String id) {
        return restTemplate.getForObject(
            baseUrl + "/api/metadata/" + id,
            MetadataResponse.class
        );
    }
} 