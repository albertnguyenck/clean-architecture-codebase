package com.example.infrastructure.persistence.jdbc.user;

import org.springframework.data.repository.CrudRepository;

public interface SpringDataUserRepository extends CrudRepository<UserEntity, String> {
} 