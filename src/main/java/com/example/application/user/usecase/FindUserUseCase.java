package com.example.application.user.usecase;

import com.example.application.user.query.FindAllUsersQuery;
import com.example.application.user.query.FindUserByEmailQuery;
import com.example.application.user.query.FindUserQuery;
import com.example.domain.user.model.User;

import java.util.List;
import java.util.Optional;

public interface FindUserUseCase {
    Optional<User> findUser(FindUserQuery query);
    Optional<User> findUserByEmail(FindUserByEmailQuery query);
    List<User> findAllUsers(FindAllUsersQuery query);
} 