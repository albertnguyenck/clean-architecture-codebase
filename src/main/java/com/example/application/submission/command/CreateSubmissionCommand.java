package com.example.application.submission.command;

import java.util.List;

public record CreateSubmissionCommand(
    String title,
    String content,
    String creatorId,
    List<String> approverIds
) {} 