package com.clinica.odontologica.repository;

import com.clinica.odontologica.model.domain.auth.ERole;
import com.clinica.odontologica.model.domain.auth.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void init() {
        user = new User(1L, "userabc", "1234", ERole.ADMIN, true, false);
    }

    @AfterEach
    public void destroyAll() {
        userRepository.deleteAll();
    }

    @Test
    public void creatUserTest() {
        User user1 = userRepository.save(user);

        assertEquals(user.getPassword(), user1.getPassword());
    }

    @Test
    public void getUserByUsernameTest() {
        User user1 = userRepository.save(user);

        Optional<User> user2 = userRepository.findByUsername(user1.getUsername());

        assertEquals(user1.getUsername(), user2.get().getUsername());
    }

    @Test
    public void getUserByIdTest() {
        User user1 = userRepository.save(user);

        Optional<User> user2 = userRepository.findById(user1.getId());

        assertEquals(user1, user2.get());
    }
}