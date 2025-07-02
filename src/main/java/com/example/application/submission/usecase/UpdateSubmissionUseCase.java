package com.example.application.submission.usecase;

import com.example.application.submission.command.UpdateSubmissionCommand;
import com.example.application.submission.dto.SubmissionId;

public interface UpdateSubmissionUseCase {
    SubmissionId updateSubmission(UpdateSubmissionCommand command);
} 