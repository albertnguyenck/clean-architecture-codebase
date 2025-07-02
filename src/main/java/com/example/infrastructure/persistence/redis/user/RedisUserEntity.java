package com.example.infrastructure.persistence.redis.user;

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
@RedisHash("user")
public class RedisUserEntity implements Serializable {
    @Id
    private String id;
    private String name;
    @Indexed
    private String email;
    private OffsetDateTime createdAt;
} 