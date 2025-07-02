package com.example.presentation.dto.request;

public record RejectSubmissionRequest(
    String approverId,
    String reason
) {
} 