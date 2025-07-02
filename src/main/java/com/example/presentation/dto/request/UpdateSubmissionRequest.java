package com.example.presentation.dto.request;

import java.util.List;

public record UpdateSubmissionRequest(
    String title,
    String content,
    List<String> approverIds
) {} 