package com.afralves.cleanarchitecture.application.usecases.boundary;

public interface UpdateUserPasswordInputBoundary {

    void updateUserPassword(String email, String newPassword);

}
