package com.example.presentation.dto.request;

import java.util.List;

public record CreateSubmissionRequest(
    String title,
    String content,
    String creatorId,
    List<String> approverIds
) {
} 