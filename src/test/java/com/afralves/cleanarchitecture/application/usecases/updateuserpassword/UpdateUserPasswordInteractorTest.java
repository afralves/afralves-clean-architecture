package com.afralves.cleanarchitecture.application.usecases.updateuserpassword;

import com.afralves.cleanarchitecture.application.exceptions.UserNotFoundException;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateUserPasswordInteractorTest {

    @Mock
    private UserGateway userGateway;

    @InjectMocks
    private UpdateUserPasswordInteractor updateUserPasswordInteractor;

    @Test
    @DisplayName("should throw exception when user not found")
    void shouldThrowExceptionWhenUserNotFound() {
        var user = aUser().build();

        when(userGateway.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> updateUserPasswordInteractor.updateUserPassword(user.getEmail(), user.getPassword())
        );
    }

    @Test
    @DisplayName("should update password successfully")
    void shouldUpdatePasswordSuccessfully() {
        var user = aUser().build();
        var newPassword = "newPassword123";

        when(userGateway.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        updateUserPasswordInteractor.updateUserPassword(user.getEmail(), newPassword);

        var captor = ArgumentCaptor.forClass(User.class);
        verify(userGateway).saveUser(captor.capture());
        assertEquals(newPassword, captor.getValue().getPassword());
    }

}
