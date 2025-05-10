package com.taskmanagement.Controller;

import com.taskmanagement.Config.JwtTokenProvider;
import com.taskmanagement.model.LoginDTO;
import com.taskmanagement.model.User;
import com.taskmanagement.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getWorkEmail(), loginDTO.getPassword())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(loginDTO.getWorkEmail());
        User user = userRepository.findByWorkEmail(loginDTO.getWorkEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        String jwt = jwtTokenProvider.generateToken(userDetails.getUsername(), user.getRole().getRoleName());

        return ResponseEntity.ok(jwt);
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(savedUser);
    }
}