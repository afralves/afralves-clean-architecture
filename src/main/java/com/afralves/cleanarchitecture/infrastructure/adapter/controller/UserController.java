package com.afralves.cleanarchitecture.infrastructure.adapter.controller;

import com.afralves.cleanarchitecture.application.usescases.CreateUserUserCase;
import com.afralves.cleanarchitecture.application.usescases.DeleteUserUserCase;
import com.afralves.cleanarchitecture.application.usescases.ListUsersUseCase;
import com.afralves.cleanarchitecture.application.usescases.UpdateUserPasswordUserCase;
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

    private final CreateUserUserCase createUserUserCase;
    private final ListUsersUseCase listUsersUseCase;
    private final UserDtoConverter userDtoConverter;
    private final DeleteUserUserCase deleteUserUserCase;
    private final UpdateUserPasswordUserCase updateUserPasswordUserCase;

    public UserController(CreateUserUserCase createUserUserCase, ListUsersUseCase listUsersUseCase, UserDtoConverter userDtoConverter, DeleteUserUserCase deleteUserUserCase, UpdateUserPasswordUserCase updateUserPasswordUserCase) {
        this.createUserUserCase = createUserUserCase;
        this.listUsersUseCase = listUsersUseCase;
        this.userDtoConverter = userDtoConverter;
        this.deleteUserUserCase = deleteUserUserCase;
        this.updateUserPasswordUserCase = updateUserPasswordUserCase;
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody CreateUserRequest request) {
        User userDomain = userDtoConverter.toUser(request);
        User user = createUserUserCase.createUser(userDomain);
        UserResponse response = userDtoConverter.toResponse(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(userDtoConverter.toResponse(user));
    }

    @PutMapping
    public ResponseEntity<Void> updateUserPassword(@RequestBody UpdateUserRequest request) {
        updateUserPasswordUserCase.updateUserPassword(request.email(), request.password());
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<ListUserResponse> getUsers() {
        List<User> users = listUsersUseCase.listUsers();
        return ResponseEntity.ok(userDtoConverter.toCreateUserResponse(users));
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteUserBy(@PathVariable String email) {
        deleteUserUserCase.deleteUserByEmail(email);
        return ResponseEntity.noContent().build();
    }

}
