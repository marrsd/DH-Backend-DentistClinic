package com.clinica.odontologica.controller.impl;

import com.clinica.odontologica.domain.auth.ERole;
import com.clinica.odontologica.domain.auth.User;
import com.clinica.odontologica.dto.UserDTO;
import com.clinica.odontologica.exception.DataAlreadyExistsException;
import com.clinica.odontologica.payload.UserRequest;
import com.clinica.odontologica.service.impl.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Tag(name = "singup-endpoint")

public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created", content = { @Content(mediaType = "text/plain", schema = @Schema(type = "string", example = "Success"))}),
            @ApiResponse(responseCode = "409", description = "The user already exists", content = @Content)
    })
    @Operation(summary = "Create a username and password either for dentist or patient before create a dentist or patient")
    @PostMapping("/register")
    public ResponseEntity<HttpStatus> createUser(@RequestBody UserRequest user) throws DataAlreadyExistsException {
            userService.createUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
