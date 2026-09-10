package com.afralves.cleanarchitecture.application.usecases.createuser;

import com.afralves.cleanarchitecture.domain.entity.User;

public record CreateUserOutput(Long id, String email, String name) {

    public static CreateUserOutput fromUser(User user) {
        return new CreateUserOutput(user.getId(), user.getEmail(), user.getName());
    }

}