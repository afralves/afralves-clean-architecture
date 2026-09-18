package com.afralves.cleanarchitecture.integration.controller;

import com.afralves.cleanarchitecture.infrastructure.adapter.persistence.model.UserEntity;
import com.afralves.cleanarchitecture.infrastructure.adapter.persistence.repository.UserRepository;
import com.afralves.cleanarchitecture.integration.AbstractIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ListUsersIT extends AbstractIntegrationTest {

    private static final String USERS_ENDPOINT = "/rest/v1/users";

    private static final String USER_ONE_NAME = "Test User One";
    private static final String USER_ONE_EMAIL = "test-user-one@example.com";
    private static final String USER_ONE_PASSWORD = "test-password-one";

    private static final String USER_TWO_NAME = "Test User Two";
    private static final String USER_TWO_EMAIL = "test-user-two@example.com";
    private static final String USER_TWO_PASSWORD = "test-password-two";

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("should return all persisted users")
    void shouldReturnAllPersistedUsers() throws Exception {
        userRepository.save(new UserEntity(null, USER_ONE_NAME, USER_ONE_PASSWORD, USER_ONE_EMAIL));
        userRepository.save(new UserEntity(null, USER_TWO_NAME, USER_TWO_PASSWORD, USER_TWO_EMAIL));

        mockMvc.perform(get(USERS_ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users", hasSize(2)))
                .andExpect(jsonPath("$.users[*].email",
                        containsInAnyOrder(USER_ONE_EMAIL, USER_TWO_EMAIL)))
                .andExpect(jsonPath("$.users[*].name",
                        containsInAnyOrder(USER_ONE_NAME, USER_TWO_NAME)));
    }

}
