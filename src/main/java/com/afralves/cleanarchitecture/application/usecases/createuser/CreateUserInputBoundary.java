package com.afralves.cleanarchitecture.application.usecases.createuser;

public interface CreateUserInputBoundary {

    CreateUserOutput createUser(CreateUserInput user);

}