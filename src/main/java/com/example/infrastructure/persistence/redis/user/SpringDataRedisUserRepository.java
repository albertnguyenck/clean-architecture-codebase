package com.example.infrastructure.persistence.redis.user;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SpringDataRedisUserRepository extends CrudRepository<RedisUserEntity, String> {
    Optional<RedisUserEntity> findByEmail(String email);
    boolean existsByEmail(String email);
} 