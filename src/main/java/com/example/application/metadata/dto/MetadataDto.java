package com.example.application.metadata.dto;

import java.time.OffsetDateTime;

public record MetadataDto(
    String id,
    String type,
    String value,
    OffsetDateTime createdAt
) {} 