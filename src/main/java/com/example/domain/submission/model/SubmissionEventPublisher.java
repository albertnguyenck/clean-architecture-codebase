package com.example.domain.submission.model;

import com.example.domain.submission.event.SubmissionCreatedEvent;

public interface SubmissionEventPublisher {
    void publishSubmissionCreated(SubmissionCreatedEvent event);
} 