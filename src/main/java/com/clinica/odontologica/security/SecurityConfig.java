package com.clinica.odontologica.security;

import com.clinica.odontologica.security.filter.AuthenticationFilter;
import com.clinica.odontologica.security.filter.ExceptionHandlerFilter;
import com.clinica.odontologica.security.filter.JWTAuthorizationFilter;
import com.clinica.odontologica.security.manager.CustomAuthenticationManager;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@AllArgsConstructor
public class SecurityConfig {
        private final CustomAuthenticationManager customAuthenticationManager;

        private String[] pathsArr1;
        private String[] pathsArr2;

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

                pathsArr1 = new String[] {
                                "/turns/new",
                                "/turns/update",
                                "/turns/delete/**"
                };

                pathsArr2 = new String[] {
                                "/patients/**",
                                "/patients/id/**",
                                "/patients/dni/**",
                                "/patients/delete/**",
                                "/dentists/**",
                                "/dentists/id/**",
                                "/dentists/registrationNumber/**",
                                "/dentists/fullName/**",
                                "/dentists/delete/**",

                                "/turns/new",
                                "/turns/all",
                                "/turns/id/**",
                                "/turns/dentistRegistrationNumber/**",
                                "/turns/patientDni/**",
                                "/turns/update",
                                "/turns/delete/**"
                };

                AuthenticationFilter authenticationFilter = new AuthenticationFilter(customAuthenticationManager);
                authenticationFilter.setFilterProcessesUrl("/authenticate");

                http
                                .csrf().disable()
                                .authorizeRequests()
                                .antMatchers("/swagger-ui/**", "/swagger-ui.html", "/api-docs/**", "/v3/api-docs/**")
                                .permitAll()
                                .antMatchers(HttpMethod.POST, "/user/singUp").permitAll()
                                .antMatchers(pathsArr1).hasAnyAuthority("USER")
                                .antMatchers(pathsArr2).hasAnyAuthority("ADMIN")
                                .anyRequest().authenticated()
                                .and()
                                .addFilterBefore(new ExceptionHandlerFilter(), AuthenticationFilter.class)
                                .addFilter(authenticationFilter)
                                .addFilterAfter(new JWTAuthorizationFilter(), AuthenticationFilter.class)
                                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

                return http.build();
        }

}
