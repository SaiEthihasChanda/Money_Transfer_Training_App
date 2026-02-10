package org.example.service;

import org.example.dtos.LoginDto;
import org.example.entity.User;
import org.example.repository.UserRepository;
import org.example.security.JwtUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("AuthService")
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger("auth-service");

    private final UserRepository userRepository;
    private final JwtUtility jwtUtility;

    @Autowired
    public AuthService(UserRepository userRepository, JwtUtility jwtUtility) {
        this.userRepository = userRepository;
        this.jwtUtility = jwtUtility;
        logger.info("UserRepository and JwtUtility injected into AuthService");
    }

    @Transactional
    public String authenticate(LoginDto loginDto) {
        // Find user by username
        User user = userRepository.findByUsername(loginDto.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        // Verify password (in production, use password encoder)
        if (!user.getPassword().equals(loginDto.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        // Generate JWT token
        String token = jwtUtility.generateToken(user.getUsername(), user.getId());
        logger.info("JWT token generated for user: {}", user.getUsername());

        return token;
    }

    @Transactional
    public String validateToken(String token) {
        if (jwtUtility.validateToken(token)) {
            String username = jwtUtility.getUsernameFromToken(token);
            logger.info("Token validated for user: {}", username);
            return username;
        }
        throw new RuntimeException("Invalid or expired token");
    }
}

