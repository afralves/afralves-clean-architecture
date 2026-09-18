package com.afralves.cleanarchitecture.integration.controller;

import com.afralves.cleanarchitecture.infrastructure.adapter.persistence.model.UserEntity;
import com.afralves.cleanarchitecture.infrastructure.adapter.persistence.repository.UserRepository;
import com.afralves.cleanarchitecture.integration.AbstractIntegrationTest;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CreateUserIT extends AbstractIntegrationTest {

    private static final String USERS_ENDPOINT = "/rest/v1/users";
    private static final String EMAIL = "test@example.com";
    private static final String PASSWORD = "test-password";
    private static final String NAME = "Test name";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateUserAndPersistIt() throws Exception {
        var payload = objectMapper.writeValueAsString(
                Map.of("email", EMAIL, "password", PASSWORD, "name", NAME));

        MvcResult result = mockMvc.perform(post(USERS_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.email").value(EMAIL))
                .andExpect(jsonPath("$.name").value(NAME))
                .andReturn();

        Long returnedId = objectMapper.readTree(result.getResponse().getContentAsString())
                .get("id").asLong();

        Optional<UserEntity> persisted = userRepository.findUserByEmail(EMAIL);
        assertThat(persisted).isPresent();
        assertThat(persisted.get().getId()).isEqualTo(returnedId);
        assertThat(persisted.get().getName()).isEqualTo(NAME);
        assertThat(persisted.get().getPassword()).isEqualTo(PASSWORD);
    }

    @Test
    void shouldReturn400WhenPasswordTooShort() throws Exception {
        var payload = objectMapper.writeValueAsString(
                Map.of("email", EMAIL, "password", "123", "name", NAME));

        mockMvc.perform(post(USERS_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.code").value("DOMAIN_VALIDATION"))
                .andExpect(jsonPath("$.message").value("Password must have at least 6 characters."));
    }

    @Test
    void shouldReturn400WhenEmailBlank() throws Exception {
        var payload = objectMapper.writeValueAsString(
                Map.of("email", " ", "password", PASSWORD, "name", NAME));

        mockMvc.perform(post(USERS_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("DOMAIN_VALIDATION"))
                .andExpect(jsonPath("$.message").value("Email must not be blank."));
    }

    @Test
    void shouldReturn400WhenNameBlank() throws Exception {
        var payload = objectMapper.writeValueAsString(
                Map.of("email", EMAIL, "password", PASSWORD, "name", " "));

        mockMvc.perform(post(USERS_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("DOMAIN_VALIDATION"))
                .andExpect(jsonPath("$.message").value("Name must not be blank."));
    }

    @Test
    void shouldReturn409WhenEmailAlreadyExists() throws Exception {
        userRepository.save(new UserEntity(null, NAME, PASSWORD, EMAIL));

        var payload = objectMapper.writeValueAsString(
                Map.of("email", EMAIL, "password", PASSWORD, "name", NAME));

        mockMvc.perform(post(USERS_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.code").value("EMAIL_ALREADY_EXISTS"))
                .andExpect(jsonPath("$.message").value("Email already registered."));
    }

}
