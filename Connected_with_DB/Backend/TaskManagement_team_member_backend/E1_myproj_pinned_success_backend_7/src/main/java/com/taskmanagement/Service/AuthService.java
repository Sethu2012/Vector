package com.taskmanagement.Service;

import com.taskmanagement.Config.JwtTokenProvider;
import com.taskmanagement.model.LoginDTO;
import com.taskmanagement.model.User;
import com.taskmanagement.Repository.UserRepository;
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
        if (userRepository.findByWorkEmail(user.getWorkEmail()).isPresent()) {
            throw new RuntimeException("User already exists");
        }
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
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