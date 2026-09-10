package com.afralves.cleanarchitecture.application.usecases.createuser;

import com.afralves.cleanarchitecture.domain.entity.User;

public interface CreateUserInputBoundary {

    User createUser(User user);

}