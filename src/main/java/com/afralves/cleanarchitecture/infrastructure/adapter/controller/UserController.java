package com.afralves.cleanarchitecture.infrastructure.adapter.controller;

import com.afralves.cleanarchitecture.application.usecases.CreateUserUseCase;
import com.afralves.cleanarchitecture.application.usecases.DeleteUserUseCase;
import com.afralves.cleanarchitecture.application.usecases.ListUsersUseCase;
import com.afralves.cleanarchitecture.application.usecases.UpdateUserPasswordUseCase;
import com.afralves.cleanarchitecture.domain.entity.User;
import com.afralves.cleanarchitecture.infrastructure.adapter.controller.converter.UserDtoConverter;
import com.afralves.cleanarchitecture.infrastructure.adapter.controller.request.CreateUserRequest;
import com.afralves.cleanarchitecture.infrastructure.adapter.controller.request.UpdateUserRequest;
import com.afralves.cleanarchitecture.infrastructure.adapter.controller.response.ListUserResponse;
import com.afralves.cleanarchitecture.infrastructure.adapter.controller.response.UserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("rest/v1/users")
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final ListUsersUseCase listUsersUseCase;
    private final UserDtoConverter userDtoConverter;
    private final DeleteUserUseCase deleteUserUseCase;
    private final UpdateUserPasswordUseCase updateUserPasswordUseCase;

    public UserController(CreateUserUseCase createUserUseCase, ListUsersUseCase listUsersUseCase, UserDtoConverter userDtoConverter, DeleteUserUseCase deleteUserUseCase, UpdateUserPasswordUseCase updateUserPasswordUseCase) {
        this.createUserUseCase = createUserUseCase;
        this.listUsersUseCase = listUsersUseCase;
        this.userDtoConverter = userDtoConverter;
        this.deleteUserUseCase = deleteUserUseCase;
        this.updateUserPasswordUseCase = updateUserPasswordUseCase;
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody CreateUserRequest request) {
        User userDomain = userDtoConverter.toUser(request);
        User user = createUserUseCase.createUser(userDomain);

        return ResponseEntity.status(HttpStatus.CREATED).body(userDtoConverter.toResponse(user));
    }

    @PutMapping
    public ResponseEntity<Void> updateUserPassword(@RequestBody UpdateUserRequest request) {
        updateUserPasswordUseCase.updateUserPassword(request.email(), request.password());
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<ListUserResponse> getUsers() {
        List<User> users = listUsersUseCase.listUsers();
        return ResponseEntity.ok(userDtoConverter.toCreateUserResponse(users));
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteUserBy(@PathVariable String email) {
        deleteUserUseCase.deleteUserByEmail(email);
        return ResponseEntity.noContent().build();
    }

}
