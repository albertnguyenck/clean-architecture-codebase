package com.example.presentation;

import com.example.application.user.command.CreateUserCommand;
import com.example.application.user.dto.UserId;
import com.example.application.user.query.FindAllUsersQuery;
import com.example.application.user.query.FindUserByEmailQuery;
import com.example.application.user.query.FindUserQuery;
import com.example.application.user.usecase.FindUserUseCase;
import com.example.application.user.usecase.UserUseCase;
import com.example.presentation.dto.request.CreateUserRequest;
import com.example.presentation.dto.response.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    
    private final UserUseCase userUseCase;
    private final FindUserUseCase findUserUseCase;
    
    public UserController(UserUseCase userUseCase, FindUserUseCase findUserUseCase) {
        this.userUseCase = userUseCase;
        this.findUserUseCase = findUserUseCase;
    }
    
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody CreateUserRequest request) {
        var command = new CreateUserCommand(
                request.name(),
                request.email()
        );
        var userId = userUseCase.createUser(command);
        var findQuery = new FindUserQuery(userId);
        Optional<UserResponse> response = findUserUseCase
                .findUser(findQuery)
                .map(UserResponse::from);
        return response.map(ResponseEntity::ok)
                .orElse(ResponseEntity.internalServerError().build());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable String id) {
        var userId = new UserId(id);
        var query = new FindUserQuery(userId);
        
        Optional<UserResponse> response = findUserUseCase
                .findUser(query)
                .map(UserResponse::from);
        
        return response.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        var query = new FindAllUsersQuery();
        
        List<UserResponse> responses = findUserUseCase
                .findAllUsers(query)
                .stream()
                .map(UserResponse::from)
                .toList();
        
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/by-email")
    public ResponseEntity<UserResponse> getUserByEmail(@RequestParam String email) {
        var query = new FindUserByEmailQuery(email);
        
        Optional<UserResponse> response = findUserUseCase
                .findUserByEmail(query)
                .map(UserResponse::from);
        
        return response.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
} 