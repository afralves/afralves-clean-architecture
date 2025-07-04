package com.afralves.cleanarchitecture.infrastructure.adapter.controller.converter;

import com.afralves.cleanarchitecture.domain.entity.User;
import com.afralves.cleanarchitecture.infrastructure.adapter.controller.request.CreateUserRequest;
import com.afralves.cleanarchitecture.infrastructure.adapter.controller.response.ListUserResponse;
import com.afralves.cleanarchitecture.infrastructure.adapter.controller.response.UserResponse;

import java.util.List;
import java.util.stream.Collectors;

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
                user.getEmail(),
                user.getName()
        );
    }

    public ListUserResponse toCreateUserResponse(List<User> users) {
        final var usersReponse = users.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return new ListUserResponse(usersReponse);
    }

}
