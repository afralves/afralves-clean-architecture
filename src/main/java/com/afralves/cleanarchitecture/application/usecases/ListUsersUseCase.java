package com.afralves.cleanarchitecture.application.usecases;

import com.afralves.cleanarchitecture.domain.entity.User;
import com.afralves.cleanarchitecture.domain.gateway.UserGateway;

import java.util.List;

public class ListUsersUseCase {

    private final UserGateway userGateway;

    public ListUsersUseCase(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    public List<User> listUsers() {
        return userGateway.findUsers();
    }
}
