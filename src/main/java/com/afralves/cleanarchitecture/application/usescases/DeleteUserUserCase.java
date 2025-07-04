package com.afralves.cleanarchitecture.application.usescases;

import com.afralves.cleanarchitecture.domain.entity.User;
import com.afralves.cleanarchitecture.domain.exceptions.EmailNotFoundException;
import com.afralves.cleanarchitecture.domain.gateway.UserGateway;

import java.util.Optional;

public class DeleteUserUserCase {

    private final UserGateway userGateway;

    public DeleteUserUserCase(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    public void deleteUserByEmail(String email) {
        Optional<User> verifyUserEmail = userGateway.findByEmail(email);

        if (verifyUserEmail.isEmpty()) {
            throw new EmailNotFoundException();
        }

        userGateway.deleteUserById(verifyUserEmail.get().getId());
    }
}
