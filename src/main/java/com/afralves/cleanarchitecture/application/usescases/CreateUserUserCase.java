package com.afralves.cleanarchitecture.application.usescases;

import com.afralves.cleanarchitecture.domain.entity.User;
import com.afralves.cleanarchitecture.domain.exceptions.EmailAlreadyExistsException;
import com.afralves.cleanarchitecture.domain.gateway.UserGateway;

import java.util.Optional;

public class CreateUserUserCase {

    private final UserGateway userGateway;

    public CreateUserUserCase(UserGateway userGateway) {
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