package com.afralves.cleanarchitecture.main;

import com.afralves.cleanarchitecture.application.usecases.CreateUserInteractor;
import com.afralves.cleanarchitecture.application.usecases.DeleteUserInteractor;
import com.afralves.cleanarchitecture.application.usecases.ListUsersInteractor;
import com.afralves.cleanarchitecture.application.usecases.UpdateUserPasswordInteractor;
import com.afralves.cleanarchitecture.application.usecases.boundary.CreateUserInputBoundary;
import com.afralves.cleanarchitecture.application.usecases.boundary.DeleteUserInputBoundary;
import com.afralves.cleanarchitecture.application.usecases.boundary.ListUsersInputBoundary;
import com.afralves.cleanarchitecture.application.usecases.boundary.UpdateUserPaasswordInputBoundary;
import com.afralves.cleanarchitecture.domain.gateway.UserGateway;
import com.afralves.cleanarchitecture.infrastructure.adapter.controller.converter.UserDtoConverter;
import com.afralves.cleanarchitecture.infrastructure.adapter.persistence.UserRepositoryAdapter;
import com.afralves.cleanarchitecture.infrastructure.adapter.persistence.converter.UserEntityConverter;
import com.afralves.cleanarchitecture.infrastructure.adapter.persistence.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserConfig {

    @Bean
    CreateUserInputBoundary createUserUserCase(UserGateway userGateway){
        return new CreateUserInteractor(userGateway);
    }

    @Bean
    UserGateway userGateway(UserRepository userRepository, UserEntityConverter userEntityConverter) {
        return new UserRepositoryAdapter(userRepository, userEntityConverter);
    }

    @Bean
    UserEntityConverter userEntityConverter() {
        return new UserEntityConverter();
    }

    @Bean
    UserDtoConverter userDtoConverter() {
        return new UserDtoConverter();
    }

    @Bean
    ListUsersInputBoundary listUsersUseCase(UserGateway userGateway) {
        return new ListUsersInteractor(userGateway);
    }

    @Bean
    DeleteUserInputBoundary deleteUserUserCase(UserGateway userGateway) {
        return new DeleteUserInteractor(userGateway);
    }

    @Bean
    UpdateUserPaasswordInputBoundary updateUserPasswordUserCase(UserGateway userGateway) {
        return new UpdateUserPasswordInteractor(userGateway);
    }


}
