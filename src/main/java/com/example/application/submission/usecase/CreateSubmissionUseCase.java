package com.example.application.submission.usecase;

import com.example.application.submission.command.CreateSubmissionCommand;
import com.example.application.submission.dto.SubmissionId;

public interface CreateSubmissionUseCase {
    SubmissionId createSubmission(CreateSubmissionCommand command);
} 