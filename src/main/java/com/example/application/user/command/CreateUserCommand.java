package com.example.application.user.command;

public record CreateUserCommand(
    String name,
    String email
) {} 