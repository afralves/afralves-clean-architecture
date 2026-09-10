package com.afralves.cleanarchitecture.application.usecases.boundary;

import com.afralves.cleanarchitecture.domain.entity.User;
import com.afralves.cleanarchitecture.domain.gateway.UserGateway;

import java.util.List;

public interface ListUsersInputBoundary {

    List<User> listUsers();

}
