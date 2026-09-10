package com.afralves.cleanarchitecture.main;

import com.afralves.cleanarchitecture.application.usecases.createuser.CreateUserInputBoundary;
import com.afralves.cleanarchitecture.application.usecases.createuser.CreateUserInteractor;
import com.afralves.cleanarchitecture.application.usecases.deleteuser.DeleteUserInputBoundary;
import com.afralves.cleanarchitecture.application.usecases.deleteuser.DeleteUserInteractor;
import com.afralves.cleanarchitecture.application.usecases.listusers.ListUsersInputBoundary;
import com.afralves.cleanarchitecture.application.usecases.listusers.ListUsersInteractor;
import com.afralves.cleanarchitecture.application.usecases.updateuserpassword.UpdateUserPasswordInputBoundary;
import com.afralves.cleanarchitecture.application.usecases.updateuserpassword.UpdateUserPasswordInteractor;
import com.afralves.cleanarchitecture.application.gateway.UserGateway;
import com.afralves.cleanarchitecture.infrastructure.adapter.controller.converter.UserDtoConverter;
import com.afralves.cleanarchitecture.infrastructure.adapter.persistence.UserRepositoryAdapter;
import com.afralves.cleanarchitecture.infrastructure.adapter.persistence.converter.UserEntityConverter;
import com.afralves.cleanarchitecture.infrastructure.adapter.persistence.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserConfig {

    @Bean
    CreateUserInputBoundary createUser(UserGateway userGateway){
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
    ListUsersInputBoundary listUsers(UserGateway userGateway) {
        return new ListUsersInteractor(userGateway);
    }

    @Bean
    DeleteUserInputBoundary deleteUser(UserGateway userGateway) {
        return new DeleteUserInteractor(userGateway);
    }

    @Bean
    UpdateUserPasswordInputBoundary updateUserPassword(UserGateway userGateway) {
        return new UpdateUserPasswordInteractor(userGateway);
    }


}
