package com.clinica.odontologica.service.impl;

import com.clinica.odontologica.model.domain.auth.ERole;
import com.clinica.odontologica.model.domain.auth.User;
import com.clinica.odontologica.exception.DataAlreadyExistsException;
import com.clinica.odontologica.exception.NoSuchDataExistsException;
import com.clinica.odontologica.payload.UserRequest;
import com.clinica.odontologica.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("UserService")
public class UserService {

    private static UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private final ObjectMapper mapper;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.mapper = new ObjectMapper();
    }

    public User createUser(UserRequest userRequest) throws DataAlreadyExistsException {
        User user = mapper.convertValue(userRequest, User.class);

        if (user.getIsAdmin())
            user.setRole(ERole.valueOf("ADMIN"));
        else
            user.setRole(ERole.valueOf("USER"));

        verifyUser(user);

        user.setAsigned(Boolean.FALSE);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    public User getUserByUsername(String username) throws NoSuchDataExistsException {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent())
            return user.get();
        else
            throw new NoSuchDataExistsException("User was not found");
    }

    public User getById(Long id) throws NoSuchDataExistsException {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent())
            return user.get();
        else
            throw new NoSuchDataExistsException("User was not found");
    }

    public static void verifyUser(User user) throws DataAlreadyExistsException {
        Optional<User> userDB = userRepository.findByUsername(user.getUsername());
        if (userDB.isPresent())
            throw new DataAlreadyExistsException("The username already exists");
    }
}
