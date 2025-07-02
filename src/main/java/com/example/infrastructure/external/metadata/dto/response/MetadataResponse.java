package com.example.infrastructure.external.metadata.dto.response;

import java.time.OffsetDateTime;

public record MetadataResponse(
    String id,
    String type,
    String value,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt
) {} 