package com.afralves.cleanarchitecture.application.usescases;

import com.afralves.cleanarchitecture.domain.exceptions.UserNotFoundException;
import com.afralves.cleanarchitecture.domain.entity.User;
import com.afralves.cleanarchitecture.domain.gateway.UserGateway;

public class UpdateUserPasswordUserCase {

    private UserGateway userGateway;

    public UpdateUserPasswordUserCase(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    public void updateUserPassword(String email, String newPassword) {
        User user = userGateway.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);
        user.changePassword(newPassword);
        userGateway.saveUser(user);
    }

}
