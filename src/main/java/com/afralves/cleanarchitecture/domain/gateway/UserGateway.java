package com.afralves.cleanarchitecture.domain.gateway;

import com.afralves.cleanarchitecture.domain.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserGateway {

    User saveUser(User user);

    List<User> findUsers();

    Optional<User> findByEmail(String email);

    void deleteUserById(Long id);

}
