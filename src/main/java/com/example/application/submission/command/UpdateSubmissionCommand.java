package com.example.application.submission.command;

import java.util.List;

public record UpdateSubmissionCommand(
    String submissionId,
    String title,
    String content,
    List<String> approverIds
) {} 