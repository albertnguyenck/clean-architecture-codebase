package com.example.infrastructure.persistence.jdbc.user;

import com.example.domain.user.model.User;
import com.example.domain.user.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Repository("jdbcUserRepository")
public class JdbcUserRepository implements UserRepository {
    private final SpringDataUserRepository springDataUserRepository;

    public JdbcUserRepository(SpringDataUserRepository springDataUserRepository) {
        this.springDataUserRepository = springDataUserRepository;
    }

    @Override
    public Optional<User> findById(String id) {
        return springDataUserRepository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public List<User> findAll() {
        return StreamSupport.stream(springDataUserRepository.findAll().spliterator(), false)
                .map(this::toDomain)
                .toList();
    }

    @Override
    public User save(User user) {
        UserEntity entity = toEntity(user);
        UserEntity savedEntity = springDataUserRepository.save(entity);
        return toDomain(savedEntity);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public boolean existsByEmail(String email) {
        return false;
    }

    private User toDomain(UserEntity entity) {
        return User.builder()
                .id(entity.getId())
                .name(entity.getName())
                .email(entity.getEmail())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    private UserEntity toEntity(User user) {
        return UserEntity.builder()
            .id(user.getId())
            .name(user.getName())
            .email(user.getEmail())
            .createdAt(user.getCreatedAt())
            .build();
    }
} 