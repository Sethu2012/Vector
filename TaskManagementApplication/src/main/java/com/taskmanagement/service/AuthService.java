package com.taskmanagement.service;

import com.taskmanagement.config.JwtTokenProvider;
import com.taskmanagement.model.LoginDTO;
import com.taskmanagement.model.User;
import com.taskmanagement.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public User registerUser(User user) {
        // Validate password
        String rawPassword = user.getPasswordHash();
        if (rawPassword == null || rawPassword.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        // Check if user already exists
        if (userRepository.findByWorkEmail(user.getWorkEmail()).isPresent()) {
            throw new RuntimeException("User already exists");
        }

        // Encode the password before saving
        user.setPasswordHash(passwordEncoder.encode(rawPassword));

        // Save the user to the repository
        return userRepository.save(user);
    }


    public String login(LoginDTO loginDTO) {
        Optional<User> userOptional = userRepository.findByWorkEmail(loginDTO.getWorkEmail());
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        User user = userOptional.get();
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid password");
        }
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
        return jwtTokenProvider.generateToken(user.getWorkEmail(), user.getRole().getRoleName());
    }
}