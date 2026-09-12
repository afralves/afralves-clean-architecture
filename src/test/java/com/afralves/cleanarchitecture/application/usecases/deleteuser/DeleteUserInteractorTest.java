package com.afralves.cleanarchitecture.application.usecases.deleteuser;

import com.afralves.cleanarchitecture.application.exceptions.EmailNotFoundException;
import com.afralves.cleanarchitecture.application.gateway.UserGateway;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.afralves.cleanarchitecture.domain.entity.UserTestBuilder.aUser;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteUserInteractorTest {

    @Mock
    private UserGateway userGateway;

    @InjectMocks
    private DeleteUserInteractor deleteUserInteractor;

    @Test
    @DisplayName("should throw exception when user is not found by email")
    void shouldThrowExceptionWhenEmailIsNotFoundByEmail() {
        final var email = "email@teste.com";

        when(userGateway.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(
                EmailNotFoundException.class,
                () -> deleteUserInteractor.deleteUserByEmail(email)
        );

        verify(userGateway, never()).deleteUserById(any());
    }

    @Test
    @DisplayName("should delete user successfully")
    void shouldDeleteUserSuccessfully() {
        final var user = aUser().withId(42L).build();

        when(userGateway.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        deleteUserInteractor.deleteUserByEmail(user.getEmail());

        verify(userGateway).deleteUserById(user.getId());
    }

}
