package com.afralves.cleanarchitecture.infrastructure.adapter.controller.response;

import java.util.List;

public record ListUserResponse(List<UserResponse> users) {}