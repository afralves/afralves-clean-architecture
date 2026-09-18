package com.afralves.cleanarchitecture.infrastructure.adapter.controller;

import com.afralves.cleanarchitecture.application.usecases.createuser.CreateUserInputBoundary;
import com.afralves.cleanarchitecture.application.usecases.deleteuser.DeleteUserInputBoundary;
import com.afralves.cleanarchitecture.application.usecases.listusers.ListUsersInputBoundary;
import com.afralves.cleanarchitecture.application.usecases.updateuserpassword.UpdateUserPasswordInputBoundary;
import com.afralves.cleanarchitecture.infrastructure.adapter.controller.request.CreateUserRequest;
import com.afralves.cleanarchitecture.infrastructure.adapter.controller.request.UpdateUserRequest;
import com.afralves.cleanarchitecture.infrastructure.adapter.controller.response.CreatedUserResponse;
import com.afralves.cleanarchitecture.infrastructure.adapter.controller.response.ListUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("rest/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final CreateUserInputBoundary createUser;
    private final ListUsersInputBoundary listUsers;
    private final DeleteUserInputBoundary deleteUser;
    private final UpdateUserPasswordInputBoundary updateUserPassword;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreatedUserResponse createUser(@RequestBody CreateUserRequest request) {
        var output = createUser.createUser(request.toCreateUserInput());
        return CreatedUserResponse.from(output);
    }

    @PutMapping
    public void updateUserPassword(@RequestBody UpdateUserRequest request) {
        updateUserPassword.updateUserPassword(request.email(), request.password());
    }

    @GetMapping
    public ListUserResponse getUsers() {
        return ListUserResponse.from(listUsers.listUsers());
    }

    @DeleteMapping("/{email}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserBy(@PathVariable String email) {
        deleteUser.deleteUserByEmail(email);
    }

}
