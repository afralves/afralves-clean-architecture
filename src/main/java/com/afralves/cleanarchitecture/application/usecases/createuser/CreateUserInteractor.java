package com.afralves.cleanarchitecture.application.usecases.createuser;

import com.afralves.cleanarchitecture.domain.entity.User;
import com.afralves.cleanarchitecture.application.exceptions.EmailAlreadyExistsException;
import com.afralves.cleanarchitecture.application.gateway.UserGateway;

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