package com.afralves.cleanarchitecture.application.usecases;

import com.afralves.cleanarchitecture.application.usecases.boundary.UpdateUserPaasswordInputBoundary;
import com.afralves.cleanarchitecture.domain.exceptions.UserNotFoundException;
import com.afralves.cleanarchitecture.domain.entity.User;
import com.afralves.cleanarchitecture.domain.gateway.UserGateway;

public class UpdateUserPasswordInteractor implements UpdateUserPaasswordInputBoundary {

    private final UserGateway userGateway;

    public UpdateUserPasswordInteractor(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    public void updateUserPassword(String email, String newPassword) {
        User user = userGateway.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);
        user.changePassword(newPassword);
        userGateway.saveUser(user);
    }

}
