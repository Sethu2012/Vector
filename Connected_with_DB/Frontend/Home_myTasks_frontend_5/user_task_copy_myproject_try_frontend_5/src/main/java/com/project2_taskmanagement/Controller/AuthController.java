package com.project2_taskmanagement.Controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.project2_taskmanagement.Model.LoginDTO;
import com.project2_taskmanagement.Model.User;
import jakarta.validation.Valid;
import java.util.Collections;
import java.util.Map;

@Controller
public class AuthController {

    private final RestTemplate restTemplate;

    @Value("${backend.api.url:http://localhost:8083}")
    private String backendApiUrl;

    public AuthController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping({"/", "/login"})
    public String showLoginPage(Model model) {
        model.addAttribute("loginDTO", new LoginDTO());
        return "index";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("loginDTO") @Valid LoginDTO loginDTO, BindingResult result, Model model) {
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
            return "redirect:/home?token=" + token;
        } catch (HttpClientErrorException e) {
            model.addAttribute("error", "Invalid email or password");
            return "index";
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred during login");
            return "index";
        }
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("user") @Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "register";
        }
        try {
            restTemplate.postForObject(backendApiUrl + "/register", user, User.class);
            return "redirect:/login?success=Registration successful";
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return "register";
        }
    }

    @GetMapping("/home")
    public String showHomePage(Model model, @RequestParam(value = "token", required = false) String token) {
        if (token == null || token.trim().isEmpty()) {
            return "redirect:/login?error=Authentication token is missing";
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            Map<String, Object> homeStats = restTemplate.exchange(
                    backendApiUrl + "/api/home/stats",
                    HttpMethod.GET,
                    entity,
                    Map.class
            ).getBody();

            model.addAttribute("userName", homeStats.get("userName"));
            model.addAttribute("recentTasks", homeStats.get("recentTasks"));
            model.addAttribute("dueTodayTasks", homeStats.get("dueTodayTasks"));
            model.addAttribute("overdueTasks", homeStats.get("overdueTasks"));
            model.addAttribute("myTasksCount", homeStats.get("myTasksCount"));
            model.addAttribute("dueTodayCount", homeStats.get("dueTodayCount"));
            model.addAttribute("projectsCount", homeStats.get("projectsCount"));
            model.addAttribute("overdueCount", homeStats.get("overdueCount"));
            model.addAttribute("activePage", "home");
            model.addAttribute("jwtToken", token);
        } catch (Exception e) {
            model.addAttribute("error", "Failed to load home data: " + e.getMessage());
            model.addAttribute("userName", "User");
            model.addAttribute("recentTasks", Collections.emptyList());
            model.addAttribute("dueTodayTasks", Collections.emptyList());
            model.addAttribute("overdueTasks", Collections.emptyList());
            model.addAttribute("myTasksCount", 0);
            model.addAttribute("dueTodayCount", 0);
            model.addAttribute("projectsCount", 0);
            model.addAttribute("overdueCount", 0);
            model.addAttribute("activePage", "home");
            model.addAttribute("jwtToken", token);
        }
        return "home";
    }

    @GetMapping("/pinnedTasks")
    public String showPinnedTasks(Model model, @RequestParam(value = "token", required = false) String token) {
        if (token == null || token.trim().isEmpty()) {
            return "redirect:/login?error=Authentication token is missing";
        }
        model.addAttribute("activePage", "pinnedTasks");
        model.addAttribute("jwtToken", token);
        return "pinnedTasks";
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login?logout=true";
    }

    @GetMapping("/profile")
    public String showProfile(Model model, @RequestParam(value = "token", required = false) String token) {
        if (token == null || token.trim().isEmpty()) {
            return "redirect:/login?error=Authentication token is missing";
        }
        model.addAttribute("activePage", "profile");
        model.addAttribute("jwtToken", token);
        return "dashboard";
    }
}