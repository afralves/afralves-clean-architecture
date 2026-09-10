package com.afralves.cleanarchitecture.application.usecases.listusers;

import com.afralves.cleanarchitecture.domain.entity.User;

import java.util.List;

public interface ListUsersInputBoundary {

    List<User> listUsers();

}
