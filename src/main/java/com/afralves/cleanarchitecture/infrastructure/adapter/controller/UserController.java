package com.afralves.cleanarchitecture.infrastructure.adapter.controller;

import com.afralves.cleanarchitecture.application.usecases.boundary.CreateUserInputBoundary;
import com.afralves.cleanarchitecture.application.usecases.boundary.DeleteUserInputBoundary;
import com.afralves.cleanarchitecture.application.usecases.boundary.ListUsersInputBoundary;
import com.afralves.cleanarchitecture.application.usecases.boundary.UpdateUserPaasswordInputBoundary;
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

    private final CreateUserInputBoundary createUser;
    private final ListUsersInputBoundary listUsers;
    private final UserDtoConverter userDtoConverter;
    private final DeleteUserInputBoundary deleteUser;
    private final UpdateUserPaasswordInputBoundary updateUserPassword;

    public UserController(CreateUserInputBoundary createUser, ListUsersInputBoundary listUsers, UserDtoConverter userDtoConverter, DeleteUserInputBoundary deleteUser, UpdateUserPaasswordInputBoundary updateUserPassword) {
        this.createUser = createUser;
        this.listUsers = listUsers;
        this.userDtoConverter = userDtoConverter;
        this.deleteUser = deleteUser;
        this.updateUserPassword = updateUserPassword;
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody CreateUserRequest request) {
        User userDomain = userDtoConverter.toUser(request);
        User user = createUser.createUser(userDomain);

        return ResponseEntity.status(HttpStatus.CREATED).body(userDtoConverter.toResponse(user));
    }

    @PutMapping
    public ResponseEntity<Void> updateUserPassword(@RequestBody UpdateUserRequest request) {
        updateUserPassword.updateUserPassword(request.email(), request.password());
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<ListUserResponse> getUsers() {
        List<User> users = listUsers.listUsers();
        return ResponseEntity.ok(userDtoConverter.toCreateUserResponse(users));
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteUserBy(@PathVariable String email) {
        deleteUser.deleteUserByEmail(email);
        return ResponseEntity.noContent().build();
    }

}
