package com.example.presentation.dto.response;

import com.example.domain.user.model.User;

public record UserResponse(
    String id,
    String name,
    String email
) {
    
    public static UserResponse from(User user) {
        return new UserResponse(
            user.getId(),
            user.getName(),
            user.getEmail()
        );
    }
} 