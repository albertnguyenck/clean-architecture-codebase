package com.example.application.user;

import com.example.application.user.command.CreateUserCommand;
import com.example.application.user.dto.UserId;
import com.example.application.user.exception.UserApplicationException;
import com.example.application.user.query.FindAllUsersQuery;
import com.example.application.user.query.FindUserByEmailQuery;
import com.example.application.user.query.FindUserQuery;
import com.example.application.user.usecase.UserUseCase;
import com.example.domain.user.model.User;
import com.example.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class UserApplicationService implements UserUseCase {
    
    private final UserRepository userRepository;
    
    public UserApplicationService(@Qualifier("jdbcUserRepository") UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @Override
    public UserId createUser(CreateUserCommand command) {
        if (userRepository.existsByEmail(command.email())) {
            throw new UserApplicationException("User with email already exists: " + command.email());
        }
        
        User user = User.builder()
                .id(UUID.randomUUID().toString())
                .name(command.name())
                .email(command.email())
                .build();
        
        User savedUser = userRepository.save(user);
        return new UserId(savedUser.getId());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<User> findUser(FindUserQuery query) {
        return userRepository.findById(query.userId().value());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<User> findUserByEmail(FindUserByEmailQuery query) {
        return userRepository.findByEmail(query.email());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<User> findAllUsers(FindAllUsersQuery query) {
        return userRepository.findAll();
    }
} 