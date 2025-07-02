package com.example.domain.submission.model;

import com.example.domain.user.model.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Getter
@EqualsAndHashCode(of = "id")
@ToString
public class Submission {
    public enum Status {
        DRAFT, SUBMITTED, APPROVED, REJECTED
    }

    private final String id;
    private String title;
    private String content;
    private final User creator;
    private final List<Approver> approvers;
    private Status status;
    private String rejectionReason;
    private final OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    private Submission(String title, String content, User creator, List<User> approvers) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.content = content;
        this.creator = creator;
        this.approvers = createApprovers(approvers);
        this.status = Status.DRAFT;
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = OffsetDateTime.now();
    }

    public static Submission create(String title, String content, User creator, List<User> approvers) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Content cannot be null or empty");
        }
        if (creator == null) {
            throw new IllegalArgumentException("Creator cannot be null");
        }
        if (approvers == null || approvers.isEmpty()) {
            throw new IllegalArgumentException("At least one approver is required");
        }
        
        return new Submission(title, content, creator, approvers);
    }

    public void submit() {
        if (status != Status.DRAFT) {
            throw new IllegalStateException("Only draft submissions can be submitted");
        }
        this.status = Status.SUBMITTED;
    }

    public void approve(User approver) {
        if (status != Status.SUBMITTED) {
            throw new IllegalStateException("Only submitted submissions can be approved");
        }
        Approver approverEntity = findApprover(approver.getId());
        if (approverEntity == null) {
            throw new IllegalArgumentException("User is not an approver for this submission");
        }
        approverEntity.approve();
        if (approvers.stream().allMatch(Approver::isApproved)) {
            this.status = Status.APPROVED;
        }
    }

    public void reject(User approver, String reason) {
        if (status != Status.SUBMITTED) {
            throw new IllegalStateException("Only submitted submissions can be rejected");
        }
        Approver approverEntity = findApprover(approver.getId());
        if (approverEntity == null) {
            throw new IllegalArgumentException("User is not an approver for this submission");
        }
        if (reason == null || reason.trim().isEmpty()) {
            throw new IllegalArgumentException("Rejection reason is required");
        }
        this.status = Status.REJECTED;
        this.rejectionReason = reason;
    }

    private Approver findApprover(String userId) {
        return approvers.stream()
                .filter(approver -> approver.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
    }

    private List<Approver> createApprovers(List<User> users) {
        List<Approver> approverList = new ArrayList<>();
        for (int i = 0; i < users.size(); i++) {
            approverList.add(Approver.builder()
                    .userId(users.get(i).getId())
                    .order(i + 1)
                    .build());
        }
        return approverList;
    }

    public List<Approver> getApprovers() {
        return Collections.unmodifiableList(approvers);
    }

    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        this.title = title;
        this.updatedAt = OffsetDateTime.now();
    }

    public void setContent(String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Content cannot be null or empty");
        }
        this.content = content;
        this.updatedAt = OffsetDateTime.now();
    }

    public void setApprovers(List<User> users) {
        if (users == null || users.isEmpty()) {
            throw new IllegalArgumentException("At least one approver is required");
        }
        this.approvers.clear();
        this.approvers.addAll(createApprovers(users));
        this.updatedAt = OffsetDateTime.now();
    }
} 