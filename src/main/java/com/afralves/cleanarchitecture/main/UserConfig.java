package com.afralves.cleanarchitecture.main;

import com.afralves.cleanarchitecture.application.usescases.CreateUserUserCase;
import com.afralves.cleanarchitecture.application.usescases.DeleteUserUserCase;
import com.afralves.cleanarchitecture.application.usescases.ListUsersUseCase;
import com.afralves.cleanarchitecture.application.usescases.UpdateUserPasswordUserCase;
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
    CreateUserUserCase createUserUserCase(UserGateway userGateway){
        return new CreateUserUserCase(userGateway);
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
    DeleteUserUserCase deleteUserUserCase(UserGateway userGateway) {
        return new DeleteUserUserCase(userGateway);
    }

    @Bean
    UpdateUserPasswordUserCase updateUserPasswordUserCase(UserGateway userGateway) {
        return new UpdateUserPasswordUserCase(userGateway);
    }


}
