package com.afralves.cleanarchitecture.infrastructure.adapter.controller.response;

import com.afralves.cleanarchitecture.application.usecases.createuser.CreateUserOutput;

public record CreatedUserResponse(Long id, String email, String name) {

    public static CreatedUserResponse from(CreateUserOutput output) {
        return new CreatedUserResponse(output.id(), output.email(), output.name());
    }
}
