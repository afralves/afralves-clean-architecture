package com.afralves.cleanarchitecture.main;

import com.afralves.cleanarchitecture.application.usecases.CreateUserUseCase;
import com.afralves.cleanarchitecture.application.usecases.DeleteUserUseCase;
import com.afralves.cleanarchitecture.application.usecases.ListUsersUseCase;
import com.afralves.cleanarchitecture.application.usecases.UpdateUserPasswordUseCase;
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
    CreateUserUseCase createUserUserCase(UserGateway userGateway){
        return new CreateUserUseCase(userGateway);
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
    ListUsersUseCase listUsersUseCase(UserGateway userGateway) {
        return new ListUsersUseCase(userGateway);
    }

    @Bean
    DeleteUserUseCase deleteUserUserCase(UserGateway userGateway) {
        return new DeleteUserUseCase(userGateway);
    }

    @Bean
    UpdateUserPasswordUseCase updateUserPasswordUserCase(UserGateway userGateway) {
        return new UpdateUserPasswordUseCase(userGateway);
    }


}
