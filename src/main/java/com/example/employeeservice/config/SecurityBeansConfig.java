package com.example.employeeservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * Configuration class dedicated to defining security-related beans.
 * <p>
 * This class is isolated from {@code SecurityConfig} to resolve circular dependency
 * issues that occur when security configurations and authentication services are
 * tightly coupled. It provides foundational beans required for the application's
 * security infrastructure.
 */
@Configuration
public class SecurityBeansConfig {

    /**
     * Creates and registers the {@link PasswordEncoder} bean.
     * <p>
     * Uses the BCrypt hashing algorithm, which is the industry standard for
     * securely hashing passwords.
     *
     * @return a configured {@link BCryptPasswordEncoder} instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Creates and registers the {@link UserDetailsService} bean.
     * <p>
     * This implementation initializes an in-memory user store with a default
     * administrative account. It requires a {@link PasswordEncoder} to ensure
     * the password is encrypted before storage.
     *
     * @param passwordEncoder the encoder used to hash the administrator's password
     * @return an {@link InMemoryUserDetailsManager} containing the configured admin user
     */
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin123"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(admin);
    }
}