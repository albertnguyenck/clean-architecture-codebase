package com.example.application.submission.usecase;

import com.example.application.submission.command.RejectSubmissionCommand;

public interface RejectSubmissionUseCase {
    void rejectSubmission(RejectSubmissionCommand command);
} 