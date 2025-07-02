package com.example.presentation.dto.common;

public record ApproverDto(
    String userId,
    int order,
    boolean approved
) {
} 