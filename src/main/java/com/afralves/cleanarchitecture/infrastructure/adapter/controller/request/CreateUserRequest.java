package com.afralves.cleanarchitecture.infrastructure.adapter.controller.request;

public record CreateUserRequest(String email, String password, String name) {}
