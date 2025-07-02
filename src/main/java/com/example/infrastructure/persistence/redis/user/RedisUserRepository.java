package com.example.infrastructure.persistence.redis.user;

import com.example.domain.user.model.User;
import com.example.domain.user.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Repository("redisUserRepository")
public class RedisUserRepository implements UserRepository {
    private final SpringDataRedisUserRepository springDataRedisUserRepository;

    public RedisUserRepository(SpringDataRedisUserRepository springDataRedisUserRepository) {
        this.springDataRedisUserRepository = springDataRedisUserRepository;
    }

    @Override
    public Optional<User> findById(String id) {
        return springDataRedisUserRepository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public List<User> findAll() {
        return StreamSupport.stream(springDataRedisUserRepository.findAll().spliterator(), false)
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return springDataRedisUserRepository.findByEmail(email)
                .map(this::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return springDataRedisUserRepository.existsByEmail(email);
    }

    @Override
    public User save(User user) {
        RedisUserEntity entity = toEntity(user);
        RedisUserEntity savedEntity = springDataRedisUserRepository.save(entity);
        return toDomain(savedEntity);
    }

    private User toDomain(RedisUserEntity entity) {
        return User.builder()
                .id(entity.getId())
                .name(entity.getName())
                .email(entity.getEmail())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    private RedisUserEntity toEntity(User user) {
        return RedisUserEntity.builder()
            .id(user.getId())
            .name(user.getName())
            .email(user.getEmail())
            .createdAt(user.getCreatedAt())
            .build();
    }
} 