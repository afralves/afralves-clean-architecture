package com.afralves.cleanarchitecture.application.usecases;

import com.afralves.cleanarchitecture.application.usecases.boundary.CreateUserInputBoundary;
import com.afralves.cleanarchitecture.domain.entity.User;
import com.afralves.cleanarchitecture.domain.exceptions.EmailAlreadyExistsException;
import com.afralves.cleanarchitecture.domain.gateway.UserGateway;

import java.util.Optional;

public class CreateUserInteractor implements CreateUserInputBoundary {

    private final UserGateway userGateway;

    public CreateUserInteractor(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    public User createUser(User user) {
        Optional<User> verifyUserEmail = userGateway.findByEmail(user.getEmail());

        if (verifyUserEmail.isPresent()) {
            throw new EmailAlreadyExistsException();
        }

        return userGateway.saveUser(user);
    }

}