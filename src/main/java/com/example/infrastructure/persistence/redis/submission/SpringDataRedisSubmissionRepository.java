package com.example.infrastructure.persistence.redis.submission;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SpringDataRedisSubmissionRepository extends CrudRepository<RedisSubmissionEntity, String> {
    List<RedisSubmissionEntity> findByCreatorId(String creatorId);
    List<RedisSubmissionEntity> findByStatus(String status);
} 