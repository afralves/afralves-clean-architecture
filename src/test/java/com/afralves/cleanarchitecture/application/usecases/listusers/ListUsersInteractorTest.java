package com.afralves.cleanarchitecture.application.usecases.listusers;

import com.afralves.cleanarchitecture.application.gateway.UserGateway;
import com.afralves.cleanarchitecture.domain.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.afralves.cleanarchitecture.domain.entity.UserTestBuilder.aUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListUsersInteractorTest {

    @Mock
    private UserGateway userGateway;

    @InjectMocks
    private ListUsersInteractor listUsersInteractor;

    @Test
    @DisplayName("should return list of users mapped to output")
    void shouldReturnListOfUsersMappedToOutput() {
        User firstUser = aUser()
                .withId(1L)
                .withEmail("alice@example.com")
                .withName("Alice")
                .build();
        User secondUser = aUser()
                .withId(2L)
                .withEmail("bob@example.com")
                .withName("Bob")
                .build();

        when(userGateway.findUsers()).thenReturn(List.of(firstUser, secondUser));

        List<ListUsersOutput> outputs = listUsersInteractor.listUsers();

        assertEquals(2, outputs.size());

        assertEquals(firstUser.getId(), outputs.get(0).id());
        assertEquals(firstUser.getEmail(), outputs.get(0).email());
        assertEquals(firstUser.getName(), outputs.get(0).name());

        assertEquals(secondUser.getId(), outputs.get(1).id());
        assertEquals(secondUser.getEmail(), outputs.get(1).email());
        assertEquals(secondUser.getName(), outputs.get(1).name());
    }

    @Test
    @DisplayName("should return empty list when no users exist")
    void shouldReturnEmptyListWhenNoUsersExist() {
        when(userGateway.findUsers()).thenReturn(List.of());

        List<ListUsersOutput> outputs = listUsersInteractor.listUsers();

        assertTrue(outputs.isEmpty());
    }

}
