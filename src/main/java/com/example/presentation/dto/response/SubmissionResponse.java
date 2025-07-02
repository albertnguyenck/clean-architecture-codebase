package com.example.presentation.dto.response;

import com.example.domain.submission.model.Submission;
import com.example.presentation.dto.common.ApproverDto;

import java.util.List;

public record SubmissionResponse(
    String id,
    String title,
    String content,
    String creatorId,
    String creatorName,
    List<ApproverDto> approvers,
    String status,
    String rejectionReason
) {
    
    public static SubmissionResponse from(Submission submission) {
        List<ApproverDto> approverDtos = submission.getApprovers().stream()
                .map(approver -> new ApproverDto(
                        approver.getUserId(), 
                        approver.getOrder(),
                        approver.isApproved()))
                .toList();
        
        return new SubmissionResponse(
            submission.getId(),
            submission.getTitle(),
            submission.getContent(),
            submission.getCreator().getId(),
            submission.getCreator().getName(),
            approverDtos,
            submission.getStatus().name(),
            submission.getRejectionReason()
        );
    }
} 