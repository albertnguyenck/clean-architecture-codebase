package com.example.application.submission.usecase;

import com.example.application.submission.dto.PaginatedSubmissionsDto;
import com.example.application.submission.query.FindAllSubmissionsQuery;
import com.example.application.submission.query.FindSubmissionQuery;
import com.example.domain.submission.model.Submission;

import java.util.Optional;

public interface FindSubmissionUseCase {
    Optional<Submission> findSubmission(FindSubmissionQuery query);
    PaginatedSubmissionsDto findAllSubmissions(FindAllSubmissionsQuery query);
} 