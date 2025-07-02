package com.example.infrastructure.persistence.redis.submission;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("submission")
public class RedisSubmissionEntity implements Serializable {
    @Id
    private String id;
    private String title;
    private String content;
    @Indexed
    private String creatorId;
    @Indexed
    private String status;
    private String rejectionReason;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
} 