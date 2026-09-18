package com.afralves.cleanarchitecture.integration.controller;

import com.afralves.cleanarchitecture.infrastructure.adapter.persistence.model.UserEntity;
import com.afralves.cleanarchitecture.infrastructure.adapter.persistence.repository.UserRepository;
import com.afralves.cleanarchitecture.integration.AbstractIntegrationTest;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UpdateUserPasswordIT extends AbstractIntegrationTest {

    private static final String USERS_ENDPOINT = "/rest/v1/users";
    private static final String EMAIL = "user@example.com";
    private static final String OLD_PASSWORD = "old-password";
    private static final String NEW_PASSWORD = "new-password";
    private static final String NAME = "User Name";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldUpdatePasswordForExistingUser() throws Exception {
        userRepository.save(new UserEntity(null, NAME, OLD_PASSWORD, EMAIL));

        var payload = objectMapper.writeValueAsString(
                Map.of("email", EMAIL, "password", NEW_PASSWORD));

        mockMvc.perform(put(USERS_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk());

        Optional<UserEntity> persisted = userRepository.findUserByEmail(EMAIL);
        assertThat(persisted).isPresent();
        assertThat(persisted.get().getPassword()).isEqualTo(NEW_PASSWORD);
        assertThat(persisted.get().getName()).isEqualTo(NAME);
    }

    @Test
    void shouldReturn404WhenUserNotFound() throws Exception {
        var payload = objectMapper.writeValueAsString(
                Map.of("email", "missing@example.com", "password", NEW_PASSWORD));

        mockMvc.perform(put(USERS_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.code").value("USER_NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("User not found."));
    }

    @Test
    void shouldReturn400WhenNewPasswordTooShort() throws Exception {
        userRepository.save(new UserEntity(null, NAME, OLD_PASSWORD, EMAIL));

        var payload = objectMapper.writeValueAsString(
                Map.of("email", EMAIL, "password", "123"));

        mockMvc.perform(put(USERS_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("DOMAIN_VALIDATION"))
                .andExpect(jsonPath("$.message").value("Password must have at least 6 characters."));
    }

}
