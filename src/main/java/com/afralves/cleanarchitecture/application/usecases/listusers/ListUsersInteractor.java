package com.afralves.cleanarchitecture.application.usecases.listusers;

import com.afralves.cleanarchitecture.application.gateway.UserGateway;

import java.util.List;

public class ListUsersInteractor implements ListUsersInputBoundary {

    private final UserGateway userGateway;

    public ListUsersInteractor(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    public List<ListUsersOutput> listUsers() {
        return userGateway.findUsers().stream()
                .map(user -> new ListUsersOutput(user.getId(), user.getEmail(), user.getName()))
                .toList();
    }
}
