package org.example.web;

import org.example.dtos.JwtTokenDto;
import org.example.dtos.LoginDto;
import org.example.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        try {
            if (loginDto.getUsername() == null || loginDto.getUsername().isEmpty()) {
                throw new RuntimeException("Username is required");
            }
            if (loginDto.getPassword() == null || loginDto.getPassword().isEmpty()) {
                throw new RuntimeException("Password is required");
            }

            String token = authService.authenticate(loginDto);
            JwtTokenDto jwtTokenDto = new JwtTokenDto(token);
            return ResponseEntity.ok(jwtTokenDto);
        } catch (RuntimeException ex) {
            Map<String, String> error = new HashMap<>();
            error.put("errorMessage", ex.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                throw new RuntimeException("Invalid authorization header format");
            }

            String token = authorizationHeader.substring(7);
            String username = authService.validateToken(token);

            Map<String, String> response = new HashMap<>();
            response.put("username", username);
            response.put("message", "Token is valid");
            return ResponseEntity.ok(response);
        } catch (RuntimeException ex) {
            Map<String, String> error = new HashMap<>();
            error.put("errorMessage", ex.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }
}

