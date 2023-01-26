package com.clinica.odontologica.securityHelper;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

public class SecurityConfigForUserTest {
    @Bean
    public SecurityFilterChain adminPageSecurity(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .headers().frameOptions().disable()
                .and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/user/register").permitAll()
                .anyRequest()
                .authenticated();

        return httpSecurity.build();
    }
}
