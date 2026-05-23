package com.example.employeeservice.controller;

import com.example.employeeservice.security.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * REST controller for handling user authentication.
 * <p>
 * This controller provides endpoints to verify user credentials and
 * issue JWT tokens upon successful authentication.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    /**
     * Constructs a new AuthController with the necessary security services.
     *
     * @param authenticationManager the manager responsible for processing auth requests
     * @param jwtService            service for generating JWT tokens
     * @param userDetailsService    service for loading user data from the database
     */
    public AuthController(AuthenticationManager authenticationManager,
                          JwtService jwtService,
                          UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Authenticates a user based on provided credentials.
     * <p>
     * If the username and password are valid, it generates and returns a JWT token.
     *
     * @param credentials a map containing 'username' and 'password'
     * @return a {@link ResponseEntity} containing the generated JWT token
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        // 1. Authenticate user using AuthenticationManager
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        // 2. Load user details
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // 3. Generate JWT token
        String token = jwtService.generateToken(userDetails);

        // 4. Return token in response
        return ResponseEntity.ok(Map.of("token", token));
    }
}