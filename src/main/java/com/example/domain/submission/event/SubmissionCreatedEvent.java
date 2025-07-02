package com.example.domain.submission.event;

import com.example.domain.submission.model.Submission;

import java.time.Instant;

public record SubmissionCreatedEvent(
    String submissionId,
    String title,
    String creatorId,
    Instant occurredAt
) {
    public static SubmissionCreatedEvent of(Submission submission) {
        return new SubmissionCreatedEvent(
            submission.getId(),
            submission.getTitle(),
            submission.getCreator().getId(),
            Instant.now()
        );
    }
} 