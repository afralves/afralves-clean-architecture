package com.afralves.cleanarchitecture.application.usecases;

import com.afralves.cleanarchitecture.application.usecases.boundary.DeleteUserInputBoundary;
import com.afralves.cleanarchitecture.domain.entity.User;
import com.afralves.cleanarchitecture.domain.exceptions.EmailNotFoundException;
import com.afralves.cleanarchitecture.domain.gateway.UserGateway;

import java.util.Optional;

public class DeleteUserInteractor implements DeleteUserInputBoundary {

    private final UserGateway userGateway;

    public DeleteUserInteractor(UserGateway userGateway) {
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
