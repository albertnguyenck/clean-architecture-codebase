package com.example.domain.user.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.OffsetDateTime;

@Builder
@Getter
@EqualsAndHashCode(of = "id")
@ToString
public class User {
    private final String id;
    private final String name;
    private final String email;
    private final OffsetDateTime createdAt;
} 