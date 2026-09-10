package com.afralves.cleanarchitecture.application.usecases.updateuserpassword;

public interface UpdateUserPasswordInputBoundary {

    void updateUserPassword(String email, String newPassword);

}
