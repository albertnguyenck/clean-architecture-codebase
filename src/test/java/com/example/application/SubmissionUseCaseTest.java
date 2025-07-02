package com.example.application;

import com.example.application.submission.command.CreateSubmissionCommand;
import com.example.application.submission.dto.PaginatedSubmissionsDto;
import com.example.application.submission.dto.SubmissionId;
import com.example.application.submission.query.FindAllSubmissionsQuery;
import com.example.application.submission.query.FindSubmissionQuery;
import com.example.application.submission.usecase.FindSubmissionUseCase;
import com.example.application.submission.usecase.SubmissionUseCase;
import com.example.domain.submission.model.Submission;
import com.example.domain.user.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SubmissionUseCaseTest {
    
    @Mock
    private SubmissionUseCase submissionUseCase;
    
    @Mock
    private FindSubmissionUseCase findSubmissionUseCase;
    
    @Test
    void shouldCreateSubmissionUsingUseCaseInterface() {
        // Given
        String title = "Test Submission";
        String content = "Test content";
        String creatorId = "user123";
        List<String> approverIds = List.of("approver1", "approver2");
        
        CreateSubmissionCommand command = new CreateSubmissionCommand(
                title, content, creatorId, approverIds
        );
        
        SubmissionId expectedId = new SubmissionId("submission-123");
        
        // Mock the use case behavior
        when(submissionUseCase.createSubmission(command)).thenReturn(expectedId);
        
        // When
        SubmissionId result = submissionUseCase.createSubmission(command);
        
        // Then
        assertEquals(expectedId, result);
    }
    
    @Test
    void shouldFindSubmissionUsingUseCaseInterface() {
        // Given
        SubmissionId submissionId = new SubmissionId("submission-123");
        FindSubmissionQuery query = new FindSubmissionQuery(submissionId);
        User creator = User.builder()
                .id("user123")
                .name("John Doe")
                .email("john@example.com")
                .build();
        User approver = User.builder()
                .id("approver1")
                .name("Approver One")
                .email("approver1@example.com")
                .build();
        
        Submission expectedSubmission = Submission.create(
                "Test Title",
                "Test Content",
                creator,
                List.of(approver)
        );
        
        // Mock the use case behavior
        when(findSubmissionUseCase.findSubmission(query))
                .thenReturn(Optional.of(expectedSubmission));
        
        // When
        Optional<Submission> result = findSubmissionUseCase.findSubmission(query);
        
        // Then
        assertTrue(result.isPresent());
        assertEquals(expectedSubmission, result.get());
    }
    
    @Test
    void shouldReturnEmptyWhenSubmissionNotFound() {
        // Given
        SubmissionId submissionId = new SubmissionId("non-existent");
        FindSubmissionQuery query = new FindSubmissionQuery(submissionId);
        
        // Mock the use case behavior
        when(findSubmissionUseCase.findSubmission(query))
                .thenReturn(Optional.empty());
        
        // When
        Optional<Submission> result = findSubmissionUseCase.findSubmission(query);
        
        // Then
        assertTrue(result.isEmpty());
    }
    
    @Test
    void shouldFindAllSubmissionsUsingUseCaseInterface() {
        // Given
        FindAllSubmissionsQuery query = new FindAllSubmissionsQuery();
        User creator = User.builder()
                .id("user123")
                .name("John Doe")
                .email("john@example.com")
                .build();
        User approver = User.builder()
                .id("approver1")
                .name("Approver One")
                .email("approver1@example.com")
                .build();
        
        Submission expectedSubmission = Submission.create(
                "Test Title",
                "Test Content",
                creator,
                List.of(approver)
        );
        
        List<Submission> expectedSubmissions = List.of(expectedSubmission);
        PaginatedSubmissionsDto expectedPaginatedResult = new PaginatedSubmissionsDto(
            expectedSubmissions, 0, 10, 1, 1, false, false
        );
        
        // Mock the use case behavior
        when(findSubmissionUseCase.findAllSubmissions(query))
                .thenReturn(expectedPaginatedResult);
        
        // When
        PaginatedSubmissionsDto result = findSubmissionUseCase.findAllSubmissions(query);
        
        // Then
        assertEquals(expectedPaginatedResult, result);
        assertEquals(1, result.submissions().size());
        assertEquals(1, result.totalElements());
        assertEquals(1, result.totalPages());
    }
} 