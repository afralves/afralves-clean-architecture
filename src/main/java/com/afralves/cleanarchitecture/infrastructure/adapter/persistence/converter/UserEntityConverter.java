package com.afralves.cleanarchitecture.infrastructure.adapter.persistence.converter;

import com.afralves.cleanarchitecture.domain.entity.User;
import com.afralves.cleanarchitecture.infrastructure.adapter.persistence.model.UserEntity;

import java.util.List;

public class UserEntityConverter {

    public User toDomain(UserEntity entity) {
        return new User(entity.getId(), entity.getEmail(), entity.getPassword(), entity.getName());
    }

    public UserEntity toEntity(User user) {
        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setEmail(user.getEmail());
        entity.setPassword(user.getPassword());
        entity.setName(user.getName());
        return entity;
    }

    public List<User> toDomain(List<UserEntity> entities) {
        return entities.stream()
                .map(this::toDomain)
                .toList();
    }

}
