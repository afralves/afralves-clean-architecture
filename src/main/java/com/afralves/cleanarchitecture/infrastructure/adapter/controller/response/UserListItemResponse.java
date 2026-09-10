package com.afralves.cleanarchitecture.infrastructure.adapter.controller.response;

import com.afralves.cleanarchitecture.application.usecases.listusers.ListUsersOutput;

public record UserListItemResponse(Long id, String email, String name) {

    public static UserListItemResponse from(ListUsersOutput output) {
        return new UserListItemResponse(output.id(), output.email(), output.name());
    }
}
