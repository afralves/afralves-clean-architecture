package com.afralves.cleanarchitecture.infrastructure.adapter.controller.request;

import com.afralves.cleanarchitecture.domain.entity.User;

public record CreateUserRequest(String email, String password, String name) {

    public User toUser() {
        return new User(email, password, name);
    }
}
