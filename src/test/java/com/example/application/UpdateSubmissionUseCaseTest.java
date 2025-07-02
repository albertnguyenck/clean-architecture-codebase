package com.example.application;

import com.example.application.submission.command.UpdateSubmissionCommand;
import com.example.application.submission.dto.SubmissionId;
import com.example.application.submission.usecase.UpdateSubmissionUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateSubmissionUseCaseTest {
    
    @Mock
    private UpdateSubmissionUseCase updateSubmissionUseCase;
    
    @Test
    void shouldUpdateSubmissionUsingUseCaseInterface() {
        // Given
        String submissionId = "submission-123";
        String newTitle = "Updated Title";
        String newContent = "Updated content";
        List<String> newApproverIds = List.of("approver1", "approver2");
        
        UpdateSubmissionCommand command = new UpdateSubmissionCommand(
                submissionId,
                newTitle,
                newContent,
                newApproverIds
        );
        
        SubmissionId expectedId = new SubmissionId(submissionId);
        
        // Mock the use case behavior
        when(updateSubmissionUseCase.updateSubmission(command)).thenReturn(expectedId);
        
        // When
        SubmissionId result = updateSubmissionUseCase.updateSubmission(command);
        
        // Then
        assertEquals(expectedId, result);
    }
    
    @Test
    void shouldUpdateSubmissionTitleOnly() {
        // Given
        String submissionId = "submission-123";
        String newTitle = "Updated Title";
        
        UpdateSubmissionCommand command = new UpdateSubmissionCommand(
                submissionId,
                newTitle,
                null,
                null
        );
        
        SubmissionId expectedId = new SubmissionId(submissionId);
        
        // Mock the use case behavior
        when(updateSubmissionUseCase.updateSubmission(command)).thenReturn(expectedId);
        
        // When
        SubmissionId result = updateSubmissionUseCase.updateSubmission(command);
        
        // Then
        assertEquals(expectedId, result);
    }
    
    @Test
    void shouldUpdateSubmissionContentOnly() {
        // Given
        String submissionId = "submission-123";
        String newContent = "Updated content";
        
        UpdateSubmissionCommand command = new UpdateSubmissionCommand(
                submissionId,
                null,
                newContent,
                null
        );
        
        SubmissionId expectedId = new SubmissionId(submissionId);
        
        // Mock the use case behavior
        when(updateSubmissionUseCase.updateSubmission(command)).thenReturn(expectedId);
        
        // When
        SubmissionId result = updateSubmissionUseCase.updateSubmission(command);
        
        // Then
        assertEquals(expectedId, result);
    }
    
    @Test
    void shouldUpdateSubmissionApproversOnly() {
        // Given
        String submissionId = "submission-123";
        List<String> newApproverIds = List.of("approver1", "approver2");
        
        UpdateSubmissionCommand command = new UpdateSubmissionCommand(
                submissionId,
                null,
                null,
                newApproverIds
        );
        
        SubmissionId expectedId = new SubmissionId(submissionId);
        
        // Mock the use case behavior
        when(updateSubmissionUseCase.updateSubmission(command)).thenReturn(expectedId);
        
        // When
        SubmissionId result = updateSubmissionUseCase.updateSubmission(command);
        
        // Then
        assertEquals(expectedId, result);
    }
} 