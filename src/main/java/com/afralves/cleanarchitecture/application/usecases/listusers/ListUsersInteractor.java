package com.afralves.cleanarchitecture.application.usecases.listusers;

import com.afralves.cleanarchitecture.domain.entity.User;
import com.afralves.cleanarchitecture.application.gateway.UserGateway;

import java.util.List;

public class ListUsersInteractor implements ListUsersInputBoundary {

    private final UserGateway userGateway;

    public ListUsersInteractor(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    public List<User> listUsers() {
        return userGateway.findUsers();
    }
}
