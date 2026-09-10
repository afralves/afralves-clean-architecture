package com.afralves.cleanarchitecture.infrastructure.adapter.controller.converter;

import com.afralves.cleanarchitecture.application.usecases.listusers.ListUsersOutput;
import com.afralves.cleanarchitecture.domain.entity.User;
import com.afralves.cleanarchitecture.infrastructure.adapter.controller.request.CreateUserRequest;
import com.afralves.cleanarchitecture.infrastructure.adapter.controller.response.ListUserResponse;
import com.afralves.cleanarchitecture.infrastructure.adapter.controller.response.UserResponse;

import java.util.List;

public class UserDtoConverter {

    public User toUser(CreateUserRequest request) {
        return new User(
                request.email(),
                request.password(),
                request.name()
        );
    }

    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getName()
        );
    }

    public ListUserResponse toCreateUserResponse(List<ListUsersOutput> users) {
        final var usersResponse = users.stream()
                .map(user -> new UserResponse(user.id(), user.email(), user.name()))
                .toList();
        return new ListUserResponse(usersResponse);
    }

}
