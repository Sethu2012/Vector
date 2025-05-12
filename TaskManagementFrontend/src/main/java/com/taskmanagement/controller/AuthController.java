package com.taskmanagement.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.taskmanagement.model.LoginDTO;
import com.taskmanagement.model.User;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import java.util.Collections;

@Controller
public class AuthController {

    private final RestTemplate restTemplate;

    @Value("${backend.api.url:http://localhost:8083}")
    private String backendApiUrl;

    public AuthController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("loginDTO", new LoginDTO());
        return "index";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("loginDTO") @Valid LoginDTO loginDTO, BindingResult result, Model model, HttpSession session) {
        if (loginDTO.getWorkEmail() == null || loginDTO.getWorkEmail().trim().isEmpty() ||
            loginDTO.getPassword() == null || loginDTO.getPassword().trim().isEmpty()) {
            model.addAttribute("error", "Fields cannot be empty");
            return "index";
        }

        if (result.hasErrors()) {
            return "index";
        }

        try {
            String token = restTemplate.postForObject(backendApiUrl + "/login", loginDTO, String.class);
            // Store token in session instead of URL
            session.setAttribute("jwtToken", token);
            return "redirect:/projects";
        } catch (HttpClientErrorException e) {
            model.addAttribute("error", "Invalid email or password");
            return "index";
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred during login");
            return "index";
        }
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("user") @Valid User user, BindingResult result, Model model) {
        try {
            restTemplate.postForObject(backendApiUrl + "/register", user, User.class);
            return "redirect:/login?success=Registration successful";
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return "register";
        }
    }

    

    @GetMapping("/pinnedTasks")
    public String showPinnedTasks(Model model) {
        model.addAttribute("activePage", "pinnedTasks");
        return "pinnedTasks";
    }
}