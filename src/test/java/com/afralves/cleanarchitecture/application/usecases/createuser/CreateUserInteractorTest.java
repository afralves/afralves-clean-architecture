package com.afralves.cleanarchitecture.application.usecases.createuser;

import com.afralves.cleanarchitecture.application.exceptions.EmailAlreadyExistsException;
import com.afralves.cleanarchitecture.application.gateway.UserGateway;
import com.afralves.cleanarchitecture.domain.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.afralves.cleanarchitecture.domain.entity.UserTestBuilder.aUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateUserInteractorTest {

    @Mock
    private UserGateway userGateway;

    @InjectMocks
    private CreateUserInteractor createUserInteractor;

    @Test
    @DisplayName("should create user successfully")
    void shouldCreateUserSuccessfully() {
        var input = new CreateUserInput("new@example.com", "password123", "New User");
        var savedUser = aUser()
                .withId(42L)
                .withEmail(input.email())
                .withPassword(input.password())
                .withName(input.name())
                .build();

        when(userGateway.findByEmail(input.email())).thenReturn(Optional.empty());
        when(userGateway.saveUser(any(User.class))).thenReturn(savedUser);

        var output = createUserInteractor.createUser(input);

        assertEquals(42L, output.id());
        assertEquals(input.email(), output.email());
        assertEquals(input.name(), output.name());

        var captor = ArgumentCaptor.forClass(User.class);
        verify(userGateway).saveUser(captor.capture());
        assertEquals(input.email(), captor.getValue().getEmail());
        assertEquals(input.password(), captor.getValue().getPassword());
        assertEquals(input.name(), captor.getValue().getName());
    }

    @Test
    @DisplayName("should throw exception when email already exists")
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        var input = new CreateUserInput("existing@example.com", "password123", "New User");
        var existingUser = aUser().withEmail(input.email()).build();

        when(userGateway.findByEmail(input.email())).thenReturn(Optional.of(existingUser));

        assertThrows(
                EmailAlreadyExistsException.class,
                () -> createUserInteractor.createUser(input)
        );

        verify(userGateway, never()).saveUser(any(User.class));
    }

}
