package com.example.application.document.dto;

import java.time.OffsetDateTime;

public record DocumentDto(
    String id,
    String title,
    String content,
    String author,
    OffsetDateTime createdAt
) {} 