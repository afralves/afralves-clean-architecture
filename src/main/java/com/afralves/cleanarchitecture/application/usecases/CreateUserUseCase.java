package com.afralves.cleanarchitecture.application.usecases;

import com.afralves.cleanarchitecture.domain.entity.User;
import com.afralves.cleanarchitecture.domain.exceptions.EmailAlreadyExistsException;
import com.afralves.cleanarchitecture.domain.gateway.UserGateway;

import java.util.Optional;

public class CreateUserUseCase {

    private final UserGateway userGateway;

    public CreateUserUseCase(UserGateway userGateway) {
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