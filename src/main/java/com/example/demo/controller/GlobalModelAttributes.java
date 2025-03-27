package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;
import java.util.Optional;

@ControllerAdvice
public class GlobalModelAttributes {

    private final UserRepository userRepository;

    public GlobalModelAttributes(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @ModelAttribute
    public void addUserInfo(Model model, Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            Optional<User> user = userRepository.findByUsername(username);
            user.ifPresent(value -> {
                model.addAttribute("isAuthenticated", true);
                model.addAttribute("username", value.getUsername());
                model.addAttribute("isAdmin", value.getRoles().contains("ADMIN"));
            });
        } else {
            model.addAttribute("isAuthenticated", false);
            model.addAttribute("isAdmin", false);
        }
    }
}

