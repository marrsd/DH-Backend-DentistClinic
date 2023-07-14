package com.clinica.odontologica.security.manager;

import com.clinica.odontologica.model.domain.auth.User;
import com.clinica.odontologica.exception.NoSuchDataExistsException;
import com.clinica.odontologica.service.impl.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CustomAuthenticationManager implements AuthenticationManager {

    private UserService userService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        try {
            User user = userService.getUserByUsername(authentication.getName());

            if (!bCryptPasswordEncoder.matches(authentication.getCredentials().toString(), user.getPassword())) {
                throw new BadCredentialsException("Wrong Password");
            }
            return new UsernamePasswordAuthenticationToken(authentication.getName(), user.getPassword(),
                    user.getAuthorities());
        } catch (NoSuchDataExistsException e) {
            throw new RuntimeException(e);
        }
    }
}
