package com.afralves.cleanarchitecture.application.usecases.listusers;

public record ListUsersOutput(
    Long id,
    String email,
    String name
) {}
