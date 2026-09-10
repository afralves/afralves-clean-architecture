package com.afralves.cleanarchitecture.infrastructure.adapter.controller.response;

import com.afralves.cleanarchitecture.application.usecases.listusers.ListUsersOutput;

import java.util.List;

public record ListUserResponse(List<UserListItemResponse> users) {

    public static ListUserResponse from(List<ListUsersOutput> outputs) {
        return new ListUserResponse(outputs.stream()
                .map(UserListItemResponse::from)
                .toList());
    }
}
