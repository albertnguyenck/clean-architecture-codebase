package com.example.domain.submission.repository;

import com.example.domain.submission.model.Submission;

import java.util.List;
import java.util.Optional;

public interface SubmissionRepository {
    Submission save(Submission submission);
    Optional<Submission> findById(String id);
    List<Submission> findAll();
} 