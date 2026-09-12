package com.afralves.cleanarchitecture.domain.entity;

import com.afralves.cleanarchitecture.domain.exception.DomainValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.afralves.cleanarchitecture.domain.entity.UserTestBuilder.aUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserTest {

    @Test
    @DisplayName("should throw exception when email is null")
    void shouldThrowExceptionWhenEmailIsNull() {
        DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> aUser().withEmail(null).build()
        );

        assertEquals("Email must not be blank.", exception.getMessage());
    }

    @Test
    @DisplayName("should throw exception when email is blank")
    void shouldThrowExceptionWhenEmailIsBlank() {
        DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> aUser().withEmail("").build()
        );

        assertEquals("Email must not be blank.", exception.getMessage());
    }

    @Test
    @DisplayName("should throw exception when password is null")
    void shouldThrowExceptionWhenPasswordIsNull() {
        DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> aUser().withPassword(null).build()
        );

        assertEquals("Password must have at least 6 characters.", exception.getMessage());
    }

    @Test
    @DisplayName("should throw exception when password has less than six characters")
    void shouldThrowExceptionWhenPasswordHasLessThanSixCharacters() {
        DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> aUser().withPassword("12345").build()
        );

        assertEquals("Password must have at least 6 characters.", exception.getMessage());
    }

    @Test
    @DisplayName("should change password")
    void shouldChangePassword() {
        var user = aUser().build();
        var newPassword = "123456789";
        user.changePassword(newPassword);

        assertEquals(newPassword, user.getPassword());
    }

    @Test
    @DisplayName("should throw exception when name is null")
    void shouldThrowExceptionWhenNameIsNull() {
        DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> aUser().withName(null).build()
        );

        assertEquals("Name must not be blank.", exception.getMessage());
    }

    @Test
    @DisplayName("should throw exception when name is blank")
    void shouldThrowExceptionWhenNameIsBlank() {
        DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> aUser().withName("").build()
        );

        assertEquals("Name must not be blank.", exception.getMessage());
    }

    @Test
    @DisplayName("should create new user")
    void shouldCreateNewUser() {
        final var user = aUser()
                .withId(1L)
                .withEmail("email@teste.com")
                .withPassword("123213213")
                .withName("Test Name")
                .build();

        assertEquals(1L, user.getId());
        assertEquals("email@teste.com", user.getEmail());
        assertEquals("123213213", user.getPassword());
        assertEquals("Test Name", user.getName());
    }

}
