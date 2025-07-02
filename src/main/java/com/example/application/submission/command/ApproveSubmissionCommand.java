package com.example.application.submission.command;

import com.example.application.submission.dto.SubmissionId;

public record ApproveSubmissionCommand(
    SubmissionId submissionId,
    String approverId
) {} 