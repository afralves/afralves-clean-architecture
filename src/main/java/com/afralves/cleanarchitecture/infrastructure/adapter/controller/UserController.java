package com.afralves.cleanarchitecture.infrastructure.adapter.controller;

import com.afralves.cleanarchitecture.application.usecases.createuser.CreateUserInputBoundary;
import com.afralves.cleanarchitecture.application.usecases.deleteuser.DeleteUserInputBoundary;
import com.afralves.cleanarchitecture.application.usecases.listusers.ListUsersInputBoundary;
import com.afralves.cleanarchitecture.application.usecases.updateuserpassword.UpdateUserPasswordInputBoundary;
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

@RestController
@RequestMapping("rest/v1/users")
public class UserController {

    private final CreateUserInputBoundary createUser;
    private final ListUsersInputBoundary listUsers;
    private final DeleteUserInputBoundary deleteUser;
    private final UpdateUserPasswordInputBoundary updateUserPassword;

    public UserController(CreateUserInputBoundary createUser, ListUsersInputBoundary listUsers, DeleteUserInputBoundary deleteUser, UpdateUserPasswordInputBoundary updateUserPassword) {
        this.createUser = createUser;
        this.listUsers = listUsers;
        this.deleteUser = deleteUser;
        this.updateUserPassword = updateUserPassword;
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody CreateUserRequest request) {
        var user = createUser.createUser(request.toUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.from(user));
    }

    @PutMapping
    public ResponseEntity<Void> updateUserPassword(@RequestBody UpdateUserRequest request) {
        updateUserPassword.updateUserPassword(request.email(), request.password());
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<ListUserResponse> getUsers() {
        return ResponseEntity.ok(ListUserResponse.from(listUsers.listUsers()));
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteUserBy(@PathVariable String email) {
        deleteUser.deleteUserByEmail(email);
        return ResponseEntity.noContent().build();
    }

}
