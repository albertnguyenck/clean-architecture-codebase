package com.example.application.submission.dto;

import com.example.domain.submission.model.Submission;
import java.util.List;

public record PaginatedSubmissionsDto(
    List<Submission> submissions,
    int page,
    int size,
    long totalElements,
    int totalPages,
    boolean hasNext,
    boolean hasPrevious
) {} 