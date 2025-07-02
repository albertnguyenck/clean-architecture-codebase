package com.example.infrastructure.messaging.kafka.producer;

import com.example.domain.submission.event.SubmissionCreatedEvent;
import com.example.domain.submission.model.SubmissionEventPublisher;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaSubmissionEventPublisher implements SubmissionEventPublisher {
    
    private static final Logger logger = LoggerFactory.getLogger(KafkaSubmissionEventPublisher.class);
    private static final String SUBMISSION_CREATED_TOPIC = "submission.created";
    
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    
    public KafkaSubmissionEventPublisher(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }
    
    @Override
    public void publishSubmissionCreated(SubmissionCreatedEvent event) {
        try {
            String message = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(SUBMISSION_CREATED_TOPIC, event.submissionId(), message);
            logger.info("Published submission created event: {}", event.submissionId());
        } catch (Exception e) {
            logger.error("Failed to publish submission created event: {}", event.submissionId(), e);
            // In a real application, you might want to handle this differently
            // (e.g., retry mechanism, dead letter queue, etc.)
        }
    }
} 