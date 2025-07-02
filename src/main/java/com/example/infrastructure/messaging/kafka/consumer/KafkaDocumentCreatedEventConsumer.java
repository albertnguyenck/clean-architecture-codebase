package com.example.infrastructure.messaging.kafka.consumer;

import com.example.application.submission.command.CreateSubmissionCommand;
import com.example.application.submission.usecase.SubmissionUseCase;
import com.example.domain.submission.event.SubmissionCreatedEvent;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KafkaDocumentCreatedEventConsumer {
    private final SubmissionUseCase submissionUseCase;

    public KafkaDocumentCreatedEventConsumer(SubmissionUseCase submissionUseCase) {
        this.submissionUseCase = submissionUseCase;
    }

    @KafkaListener(topics = "document-created", groupId = "submission-service")
    public void handleDocumentCreatedEvent(ConsumerRecord<String, String> record) {
        String eventJson = record.value();
        DocumentCreatedEvent event = DocumentCreatedEvent.fromJson(eventJson);
        CreateSubmissionCommand command = new CreateSubmissionCommand(
                event.title(),
                event.content(),
                event.creatorId(),
                List.of(event.creatorId())
        );
        submissionUseCase.createSubmission(command);
    }

    public record DocumentCreatedEvent(String title, String content, String creatorId) {
        public static DocumentCreatedEvent fromJson(String json) {
            // Implement JSON parsing logic here
            return null;
        }
    }
} 