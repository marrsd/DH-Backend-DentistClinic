package com.clinica.odontologica.service.impl;

import com.clinica.odontologica.model.domain.auth.ERole;
import com.clinica.odontologica.model.domain.auth.User;
import com.clinica.odontologica.exception.DataAlreadyExistsException;
import com.clinica.odontologica.exception.NoSuchDataExistsException;
import com.clinica.odontologica.payload.UserRequest;
import com.clinica.odontologica.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Mock
    ObjectMapper mapper;

    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    private User user;
    private UserRequest userRequest;

    @BeforeEach
    public void init() {
        user = new User(1L, "userbgh", "12345", ERole.ADMIN, true, false);

        userRequest = new UserRequest("userbgh", "12345", true);
    }

    @Test
    @Order(1)
    public void createUserTest() throws Exception {
        when(userRepository.save(any(User.class))).thenReturn(user);

        User user1 = userService.createUser(userRequest);

        assertEquals(user1.getUsername(), user.getUsername());
    }

    @Test
    @Order(2)
    public void conflictCreateUserTest() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        assertThrows(DataAlreadyExistsException.class, () -> userService.createUser(userRequest));
    }

    @Test
    @Order(3)
    public void getUserByUsernameTest() throws Exception {
        String username = "userbgh";

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        User user1 = userService.getUserByUsername(username);

        assertNotNull(user1);
    }

    @Test
    @Order(4)
    public void notFoundUserByUsernameTest() {
        String username = "userhjp";

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(NoSuchDataExistsException.class, () -> userService.getUserByUsername(username));
    }

    @Test
    @Order(5)
    public void getUserByIdTest() throws Exception {
        Long id = 1L;

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        User user1 = userService.getById(id);

        assertNotNull(user1);
    }

    @Test
    @Order(6)
    public void notFoundUserByIdTest() {
        Long id = 30L;

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NoSuchDataExistsException.class, () -> userService.getById(id));
    }
}
