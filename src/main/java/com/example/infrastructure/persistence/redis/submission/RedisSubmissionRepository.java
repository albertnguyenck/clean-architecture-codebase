package com.example.infrastructure.persistence.redis.submission;

import com.example.domain.submission.model.Submission;
import com.example.domain.submission.repository.SubmissionRepository;
import com.example.domain.user.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Repository("redisSubmissionRepository")
public class RedisSubmissionRepository implements SubmissionRepository {
    private final SpringDataRedisSubmissionRepository springDataRedisSubmissionRepository;

    public RedisSubmissionRepository(SpringDataRedisSubmissionRepository springDataRedisSubmissionRepository) {
        this.springDataRedisSubmissionRepository = springDataRedisSubmissionRepository;
    }

    @Override
    public Optional<Submission> findById(String id) {
        return springDataRedisSubmissionRepository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public List<Submission> findAll() {
        return StreamSupport.stream(springDataRedisSubmissionRepository.findAll().spliterator(), false)
                .map(this::toDomain)
                .toList();
    }

    @Override
    public Submission save(Submission submission) {
        RedisSubmissionEntity entity = toEntity(submission);
        RedisSubmissionEntity savedEntity = springDataRedisSubmissionRepository.save(entity);
        return toDomain(savedEntity);
    }

    private Submission toDomain(RedisSubmissionEntity entity) {
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

    private RedisSubmissionEntity toEntity(Submission submission) {
        return RedisSubmissionEntity.builder()
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