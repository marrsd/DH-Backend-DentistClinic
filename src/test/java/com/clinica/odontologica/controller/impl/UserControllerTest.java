package com.clinica.odontologica.controller.impl;

import com.clinica.odontologica.security.SecurityConfig;
import com.clinica.odontologica.model.domain.auth.User;
import com.clinica.odontologica.payload.UserRequest;
import com.clinica.odontologica.security.manager.CustomAuthenticationManager;
import com.clinica.odontologica.service.impl.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(SecurityConfig.class)
@WebMvcTest(UserController.class)
class UserControllerTest {

    @MockBean
    UserService userService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    CustomAuthenticationManager customAuthenticationManager;

    private UserRequest userRequest;

    @BeforeEach
    public void setup() {

        userRequest = new UserRequest("user123", "36gd83", false);
    }

    @Test
    public void createUser() throws Exception {
        User user = objectMapper.convertValue(userRequest, User.class);
        when(userService.createUser(any(UserRequest.class))).thenReturn(user);

        String payloadUser = objectMapper.writeValueAsString(userRequest);

        this.mockMvc.perform(post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payloadUser))
                .andExpect(status().isCreated())
                .andReturn();
    }

}