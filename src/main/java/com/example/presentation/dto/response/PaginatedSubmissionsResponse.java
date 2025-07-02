package com.example.presentation.dto.response;

import java.util.List;

public record PaginatedSubmissionsResponse(
    List<SubmissionResponse> submissions,
    int page,
    int size,
    long totalElements,
    int totalPages,
    boolean hasNext,
    boolean hasPrevious
) {} 