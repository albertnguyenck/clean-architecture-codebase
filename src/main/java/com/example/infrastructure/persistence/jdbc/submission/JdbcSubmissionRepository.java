package com.example.infrastructure.persistence.jdbc.submission;

import com.example.domain.submission.model.Submission;
import com.example.domain.submission.repository.SubmissionRepository;
import com.example.domain.user.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Repository("jdbcSubmissionRepository")
public class JdbcSubmissionRepository implements SubmissionRepository {
    private final SpringDataSubmissionRepository springDataSubmissionRepository;

    public JdbcSubmissionRepository(SpringDataSubmissionRepository springDataSubmissionRepository) {
        this.springDataSubmissionRepository = springDataSubmissionRepository;
    }

    @Override
    public Optional<Submission> findById(String id) {
        return springDataSubmissionRepository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public List<Submission> findAll() {
        return StreamSupport.stream(springDataSubmissionRepository.findAll().spliterator(), false)
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Submission save(Submission submission) {
        SubmissionEntity entity = toEntity(submission);
        SubmissionEntity savedEntity = springDataSubmissionRepository.save(entity);
        return toDomain(savedEntity);
    }

    private Submission toDomain(SubmissionEntity entity) {
        User creator = User.builder()
                .id(entity.getCreatorId())
                .name("Unknown")
                .email("unknown@example.com")
                .createdAt(entity.getCreatedAt())
                .build();
        List<User> approvers = new ArrayList<>();
        
        // Note: This is a simplified conversion. In a real application,
        // you would need to load the actual creator and approvers from their repositories
        Submission submission = Submission.create(
                entity.getTitle(),
                entity.getContent(),
                creator,
                approvers
        );
        
        // Set the status and other fields that are managed by the domain
        if (entity.getStatus() != null) {
            // The domain model should handle status transitions
        }
        
        return submission;
    }

    private SubmissionEntity toEntity(Submission submission) {
        return SubmissionEntity.builder()
            .id(submission.getId())
            .title(submission.getTitle())
            .content(submission.getContent())
            .creatorId(submission.getCreator().getId())
            .status(submission.getStatus().name())
            .rejectionReason(submission.getRejectionReason())
            .createdAt(submission.getCreatedAt())
            .updatedAt(submission.getUpdatedAt())
            .build();
    }
} 