package com.afralves.cleanarchitecture.application.usecases.createuser;

import com.afralves.cleanarchitecture.domain.entity.User;

public record CreateUserInput(String email, String password, String name) {

    public User toUser() {
        return new User(email, password, name);
    }
}


