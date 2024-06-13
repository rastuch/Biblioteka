package com.studia.biblioteka;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@TestConfiguration
public class NoSecurityConfig {
    @Bean
    public SecurityFilterChain disableSpringSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeRequests(authorize -> authorize.anyRequest().permitAll())
                .build();
    }
}