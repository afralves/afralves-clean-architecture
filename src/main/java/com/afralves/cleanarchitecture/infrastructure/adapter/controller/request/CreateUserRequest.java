package com.afralves.cleanarchitecture.infrastructure.adapter.controller.request;

import com.afralves.cleanarchitecture.application.usecases.createuser.CreateUserInput;

public record CreateUserRequest(String email, String password, String name) {

    public CreateUserInput toCreateUserInput() {
        return new CreateUserInput(email, password, name);
    }

}
