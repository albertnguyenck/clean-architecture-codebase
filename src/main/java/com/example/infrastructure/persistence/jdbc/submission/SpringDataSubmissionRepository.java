package com.example.infrastructure.persistence.jdbc.submission;

import org.springframework.data.repository.CrudRepository;

public interface SpringDataSubmissionRepository extends CrudRepository<SubmissionEntity, String> {
}