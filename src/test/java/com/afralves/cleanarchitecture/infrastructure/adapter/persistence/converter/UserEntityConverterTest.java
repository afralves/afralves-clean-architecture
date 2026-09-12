package com.afralves.cleanarchitecture.infrastructure.adapter.persistence.converter;

import com.afralves.cleanarchitecture.domain.entity.User;
import com.afralves.cleanarchitecture.infrastructure.adapter.persistence.model.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.afralves.cleanarchitecture.domain.entity.UserTestBuilder.aUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserEntityConverterTest {

    private final UserEntityConverter converter = new UserEntityConverter();

    @Test
    @DisplayName("should convert User to UserEntity preserving all fields")
    void shouldConvertUserToUserEntity() {
        User user = aUser()
                .withId(42L)
                .withEmail("alice@example.com")
                .withPassword("password123")
                .withName("Alice")
                .build();

        UserEntity entity = converter.toEntity(user);

        assertEquals(user.getId(), entity.getId());
        assertEquals(user.getEmail(), entity.getEmail());
        assertEquals(user.getPassword(), entity.getPassword());
        assertEquals(user.getName(), entity.getName());
    }

    @Test
    @DisplayName("should convert UserEntity to User preserving all fields")
    void shouldConvertUserEntityToUser() {
        UserEntity entity = new UserEntity(7L, "Bob", "password456", "bob@example.com");

        User user = converter.toDomain(entity);

        assertEquals(entity.getId(), user.getId());
        assertEquals(entity.getEmail(), user.getEmail());
        assertEquals(entity.getPassword(), user.getPassword());
        assertEquals(entity.getName(), user.getName());
    }

    @Test
    @DisplayName("should convert list of UserEntity to User preserving order and fields")
    void shouldConvertListOfUserEntityToUser() {
        UserEntity first = new UserEntity(1L, "Alice", "password123", "alice@example.com");
        UserEntity second = new UserEntity(2L, "Bob", "password456", "bob@example.com");

        List<User> users = converter.toDomain(List.of(first, second));

        assertEquals(2, users.size());

        assertEquals(first.getId(), users.getFirst().getId());
        assertEquals(first.getEmail(), users.getFirst().getEmail());
        assertEquals(first.getPassword(), users.getFirst().getPassword());
        assertEquals(first.getName(), users.getFirst().getName());

        assertEquals(second.getId(), users.get(1).getId());
        assertEquals(second.getEmail(), users.get(1).getEmail());
        assertEquals(second.getPassword(), users.get(1).getPassword());
        assertEquals(second.getName(), users.get(1).getName());
    }

    @Test
    @DisplayName("should return empty list when input list is empty")
    void shouldReturnEmptyListWhenInputIsEmpty() {
        List<User> users = converter.toDomain(List.of());

        assertTrue(users.isEmpty());
    }

    @Test
    @DisplayName("should preserve all fields when converting to entity and back to domain")
    void shouldPreserveAllFieldsWhenConvertingToEntityAndBackToDomain() {
        User original = aUser()
                .withId(99L)
                .withEmail("roundtrip@example.com")
                .withPassword("password789")
                .withName("Round Trip")
                .build();

        User result = converter.toDomain(converter.toEntity(original));

        assertEquals(original.getId(), result.getId());
        assertEquals(original.getEmail(), result.getEmail());
        assertEquals(original.getPassword(), result.getPassword());
        assertEquals(original.getName(), result.getName());
    }

}
