package com.example.domain;

import com.example.domain.submission.model.Approver;
import com.example.domain.submission.model.Submission;
import com.example.domain.user.model.User;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SubmissionTest {
    
    @Test
    void shouldCreateSubmissionWithValidData() {
        // Given
        String title = "Test Submission";
        String content = "This is a test submission content";
        User creator = User.builder()
                .id("user123")
                .name("John Doe")
                .email("john@example.com")
                .build();
        List<User> approvers = Arrays.asList(
            User.builder().id("approver1").name("Approver One").email("approver1@example.com").build(),
            User.builder().id("approver2").name("Approver Two").email("approver2@example.com").build()
        );
        
        // When
        Submission submission = Submission.create(
                title,
                content,
                creator,
                approvers
        );
        
        // Then
        assertNotNull(submission.getId());
        assertEquals(title, submission.getTitle());
        assertEquals(content, submission.getContent());
        assertEquals(creator.getId(), submission.getCreator().getId());
        assertEquals(Submission.Status.DRAFT, submission.getStatus());
        assertEquals(2, submission.getApprovers().size());
        assertEquals("approver1", submission.getApprovers().get(0).getUserId());
        assertEquals(1, submission.getApprovers().get(0).getOrder());
        assertFalse(submission.getApprovers().get(0).isApproved());
    }
    
    @Test
    void shouldReturnImmutableApproversList() {
        // Given
        User creator = User.builder()
                .id("creator")
                .name("Creator")
                .email("creator@example.com")
                .build();
        List<User> approvers = Collections.singletonList(User.builder().id("approver1").name("Approver One").email("approver1@example.com").build());
        Submission submission = Submission.create(
                "Title",
                "Content",
                creator,
                approvers
        );
        
        // When & Then
        assertThrows(UnsupportedOperationException.class, () -> {
            submission.getApprovers().add(Approver.builder().userId("approver2").order(2).build());
        });
    }
    
    @Test
    void shouldSubmitSubmission() {
        // Given
        User creator = User.builder()
                .id("creator")
                .name("Creator")
                .email("creator@example.com")
                .build();
        List<User> approvers = Arrays.asList(User.builder().id("approver1").name("Approver One").email("approver1@example.com").build());
        Submission submission = Submission.create(
                "Title",
                "Content",
                creator,
                approvers
        );
        
        // When
        submission.submit();
        
        // Then
        assertEquals(Submission.Status.SUBMITTED, submission.getStatus());
    }
    
    @Test
    void shouldApproveSubmission() {
        // Given
        User creator = User.builder()
                .id("creator")
                .name("Creator")
                .email("creator@example.com")
                .build();
        User approver = User.builder()
                .id("approver1")
                .name("Approver One")
                .email("approver1@example.com")
                .build();
        List<User> approvers = Collections.singletonList(approver);
        Submission submission = Submission.create(
                "Title",
                "Content",
                creator,
                approvers
        );
        submission.submit();
        
        // When
        submission.approve(approver);
        
        // Then
        assertEquals(Submission.Status.APPROVED, submission.getStatus());
        assertTrue(submission.getApprovers().get(0).isApproved());
    }
    
    @Test
    void shouldRejectSubmission() {
        // Given
        User creator = User.builder()
                .id("creator")
                .name("Creator")
                .email("creator@example.com")
                .build();
        User approver = User.builder()
                .id("approver1")
                .name("Approver One")
                .email("approver1@example.com")
                .build();
        List<User> approvers = Collections.singletonList(approver);
        Submission submission = Submission.create(
                "Title",
                "Content",
                creator,
                approvers
        );
        submission.submit();
        
        // When
        submission.reject(approver, "Invalid content");
        
        // Then
        assertEquals(Submission.Status.REJECTED, submission.getStatus());
        assertEquals("Invalid content", submission.getRejectionReason());
    }
    
    @Test
    void shouldUpdateSubmissionTitle() {
        // Given
        User creator = User.builder()
                .id("creator")
                .name("Creator")
                .email("creator@example.com")
                .build();
        List<User> approvers = Collections.singletonList(User.builder().id("approver1").name("Approver One").email("approver1@example.com").build());
        Submission submission = Submission.create(
                "Original Title",
                "Content",
                creator,
                approvers
        );
        
        // When
        submission.setTitle("Updated Title");
        
        // Then
        assertEquals("Updated Title", submission.getTitle());
    }
    
    @Test
    void shouldUpdateSubmissionContent() {
        // Given
        User creator = User.builder()
                .id("creator")
                .name("Creator")
                .email("creator@example.com")
                .build();
        List<User> approvers = Collections.singletonList(User.builder().id("approver1").name("Approver One").email("approver1@example.com").build());
        Submission submission = Submission.create(
                "Title",
                "Original Content",
                creator,
                approvers
        );
        
        // When
        submission.setContent("Updated Content");
        
        // Then
        assertEquals("Updated Content", submission.getContent());
    }
    
    @Test
    void shouldUpdateSubmissionApprovers() {
        // Given
        User creator = User.builder()
                .id("creator")
                .name("Creator")
                .email("creator@example.com")
                .build();
        List<User> originalApprovers = Collections.singletonList(User.builder().id("approver1").name("Approver One").email("approver1@example.com").build());
        Submission submission = Submission.create(
                "Title",
                "Content",
                creator,
                originalApprovers
        );
        
        List<User> newApprovers = Arrays.asList(
                User.builder().id("approver2").name("Approver Two").email("approver2@example.com").build(),
                User.builder().id("approver3").name("Approver Three").email("approver3@example.com").build()
        );
        
        // When
        submission.setApprovers(newApprovers);
        
        // Then
        assertEquals(2, submission.getApprovers().size());
        assertEquals("approver2", submission.getApprovers().get(0).getUserId());
        assertEquals("approver3", submission.getApprovers().get(1).getUserId());
        assertEquals(1, submission.getApprovers().get(0).getOrder());
        assertEquals(2, submission.getApprovers().get(1).getOrder());
    }
    
    @Test
    void shouldThrowExceptionWhenSettingEmptyTitle() {
        // Given
        User creator = User.builder()
                .id("creator")
                .name("Creator")
                .email("creator@example.com")
                .build();
        List<User> approvers = Collections.singletonList(User.builder().id("approver1").name("Approver One").email("approver1@example.com").build());
        Submission submission = Submission.create(
                "Title",
                "Content",
                creator,
                approvers
        );
        
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            submission.setTitle("");
        });
    }
    
    @Test
    void shouldThrowExceptionWhenSettingEmptyContent() {
        // Given
        User creator = User.builder()
                .id("creator")
                .name("Creator")
                .email("creator@example.com")
                .build();
        List<User> approvers = Collections.singletonList(User.builder().id("approver1").name("Approver One").email("approver1@example.com").build());
        Submission submission = Submission.create(
                "Title",
                "Content",
                creator,
                approvers
        );
        
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            submission.setContent("");
        });
    }
    
    @Test
    void shouldThrowExceptionWhenSettingEmptyApprovers() {
        // Given
        User creator = User.builder()
                .id("creator")
                .name("Creator")
                .email("creator@example.com")
                .build();
        List<User> approvers = Collections.singletonList(User.builder().id("approver1").name("Approver One").email("approver1@example.com").build());
        Submission submission = Submission.create(
                "Title",
                "Content",
                creator,
                approvers
        );
        
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            submission.setApprovers(List.of());
        });
    }
} 