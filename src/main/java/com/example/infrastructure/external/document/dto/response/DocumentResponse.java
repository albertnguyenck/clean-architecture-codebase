package com.example.infrastructure.external.document.dto.response;

import java.time.OffsetDateTime;

public record DocumentResponse(
    String id,
    String title,
    String content,
    String author,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt
) {} 