package com.example.application.user.usecase;

import com.example.application.user.command.CreateUserCommand;
import com.example.application.user.dto.UserId;

public interface CreateUserUseCase {
    UserId createUser(CreateUserCommand command);
} 