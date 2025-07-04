package com.afralves.cleanarchitecture.infrastructure.adapter.persistence.repository;

import com.afralves.cleanarchitecture.infrastructure.adapter.persistence.model.UserEntity;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends ListCrudRepository<UserEntity, Long> {

    Optional<UserEntity> findUserByEmail(String email);

}
