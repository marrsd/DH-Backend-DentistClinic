package com.clinica.odontologica.securityHelper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfigForTest {
    String[] pathsArr1 = new String[] {
        "/patients/new",
            "/patients/update",
                "/dentists/new",
                "/dentists/update",
                "/turns/new",
                "/turns/update",
                "/turns/delete"
    };

    String[] pathsArr2 = new String[] {
        "/patients/**",
                "/dentists/**",
                "/turns/**"
    };

    @Bean
    public SecurityFilterChain restApiSecurity(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .authorizeHttpRequests()
                .antMatchers(pathsArr1).hasAnyAuthority("USER", "ADMIN")
                .antMatchers(pathsArr2).hasAnyAuthority("ADMIN")
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .and()
                .oauth2ResourceServer()
                .jwt();

        return httpSecurity.build();
    }
}
