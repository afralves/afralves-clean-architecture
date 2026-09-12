package com.afralves.cleanarchitecture.application.usecases.updateuserpassword;

import com.afralves.cleanarchitecture.application.exceptions.UserNotFoundException;
import com.afralves.cleanarchitecture.domain.entity.User;
import com.afralves.cleanarchitecture.application.gateway.UserGateway;

public class UpdateUserPasswordInteractor implements UpdateUserPasswordInputBoundary {

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
