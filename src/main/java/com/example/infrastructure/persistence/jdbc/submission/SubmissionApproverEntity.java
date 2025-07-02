package com.example.infrastructure.persistence.jdbc.submission;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("submission_approvers")
public class SubmissionApproverEntity {
    @Id
    private Long id;
    private String submissionId;
    private String userId;
    private Integer approvalOrder;
    private Boolean approved;
    private OffsetDateTime createdAt;
} 