package com.afralves.cleanarchitecture.infrastructure.adapter.controller.response;

import com.afralves.cleanarchitecture.application.usecases.listusers.ListUsersOutput;
import com.afralves.cleanarchitecture.domain.entity.User;

public record UserResponse(Long id, String email, String name) {

    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getEmail(), user.getName());
    }

    public static UserResponse from(ListUsersOutput output) {
        return new UserResponse(output.id(), output.email(), output.name());
    }
}
