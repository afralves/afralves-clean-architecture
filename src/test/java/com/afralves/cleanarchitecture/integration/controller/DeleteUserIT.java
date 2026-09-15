package com.afralves.cleanarchitecture.integration.controller;

import com.afralves.cleanarchitecture.infrastructure.adapter.persistence.model.UserEntity;
import com.afralves.cleanarchitecture.infrastructure.adapter.persistence.repository.UserRepository;
import com.afralves.cleanarchitecture.integration.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DeleteUserIT extends AbstractIntegrationTest {

    private static final String USERS_ENDPOINT = "/rest/v1/users";
    private static final String EMAIL = "to-delete@example.com";
    private static final String PASSWORD = "some-password";
    private static final String NAME = "Name Test";

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldDeleteUserByEmail() throws Exception {
        userRepository.save(new UserEntity(null, NAME, PASSWORD, EMAIL));
        assertThat(userRepository.findUserByEmail(EMAIL)).isPresent();

        mockMvc.perform(delete(USERS_ENDPOINT + "/" + EMAIL))
                .andExpect(status().isNoContent());

        assertThat(userRepository.findUserByEmail(EMAIL)).isEmpty();
    }

}
