package com.afralves.cleanarchitecture.infrastructure.adapter.persistence;

import com.afralves.cleanarchitecture.domain.entity.User;
import com.afralves.cleanarchitecture.domain.gateway.UserGateway;
import com.afralves.cleanarchitecture.infrastructure.adapter.persistence.converter.UserEntityConverter;
import com.afralves.cleanarchitecture.infrastructure.adapter.persistence.model.UserEntity;
import com.afralves.cleanarchitecture.infrastructure.adapter.persistence.repository.UserRepository;

import java.util.List;
import java.util.Optional;

public class UserRepositoryAdapter implements UserGateway {

    private final UserRepository userRepository;
    private final UserEntityConverter converter;

    public UserRepositoryAdapter(UserRepository userRepository, UserEntityConverter converter) {
        this.userRepository = userRepository;
        this.converter = converter;
    }

    @Override
    public User saveUser(User user) {
        UserEntity userEntity = converter.toEntity(user);
        UserEntity userSaved = userRepository.save(userEntity);
        return converter.toDomain(userSaved);
    }

    @Override
    public List<User> findUsers() {
        List<UserEntity> userSaved = userRepository.findAll();
        return converter.toDomain(userSaved);
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .map(converter::toDomain);
    }

}
