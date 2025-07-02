package com.example.application.submission.usecase;

import com.example.application.submission.command.ApproveSubmissionCommand;

public interface ApproveSubmissionUseCase {
    void approveSubmission(ApproveSubmissionCommand command);
} 