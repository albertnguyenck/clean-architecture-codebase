package com.example.infrastructure.messaging.kafka.consumer;

import com.example.application.submission.usecase.SubmissionUseCase;
import com.example.application.submission.command.CreateSubmissionCommand;
import com.example.application.submission.dto.SubmissionId;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KafkaDocumentCreatedEventConsumer {
    
    private static final Logger logger = LoggerFactory.getLogger(KafkaDocumentCreatedEventConsumer.class);
    
    private final SubmissionUseCase submissionUseCase;
    private final ObjectMapper objectMapper;
    
    public KafkaDocumentCreatedEventConsumer(SubmissionUseCase submissionUseCase, ObjectMapper objectMapper) {
        this.submissionUseCase = submissionUseCase;
        this.objectMapper = objectMapper;
    }
    
    @KafkaListener(topics = "document.created", groupId = "submission-service")
    public void handleDocumentCreated(String message) {
        try {
            logger.info("Received document created event: {}", message);
            
            // Parse the incoming event
            DocumentCreatedEvent event = objectMapper.readValue(message, DocumentCreatedEvent.class);
            
            // Create a submission based on the document
            CreateSubmissionCommand command = new CreateSubmissionCommand(
                "Submission for Document: " + event.documentId(),
                "Auto-generated submission for document: " + event.documentId(),
                event.creatorId(),
                List.of(event.creatorId()) // Default approver is the creator
            );
            
            SubmissionId submissionId = submissionUseCase.createSubmission(command);
            logger.info("Created submission {} for document {}", submissionId.value(), event.documentId());
            
        } catch (Exception e) {
            logger.error("Failed to process document created event: {}", message, e);
            // In a real application, you might want to:
            // - Send to dead letter queue
            // - Retry with exponential backoff
            // - Alert monitoring systems
        }
    }
    
    // DTO for the incoming event
    public record DocumentCreatedEvent(
        String documentId,
        String creatorId,
        String documentType,
        String timestamp
    ) {}
} 