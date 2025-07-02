package com.example.application.submission.command;

import com.example.application.submission.dto.SubmissionId;

public record RejectSubmissionCommand(
    SubmissionId submissionId,
    String approverId,
    String reason
) {} 