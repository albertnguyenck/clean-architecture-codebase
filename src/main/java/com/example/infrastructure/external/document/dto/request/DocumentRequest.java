package com.example.infrastructure.external.document.dto.request;

public record DocumentRequest(
    String title,
    String content,
    String author
) {} 