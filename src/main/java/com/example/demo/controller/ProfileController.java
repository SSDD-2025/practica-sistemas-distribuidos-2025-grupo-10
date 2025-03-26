package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @GetMapping
    public String showProfile(Model model, Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            User user = userRepository.findByUsername(username).orElse(null);
            if (user != null) {
                model.addAttribute("user", user);
                return "profile";
            }
        }
        return "redirect:/login";
    }

    @PostMapping("/delete")
    public String deleteAccount(Principal principal, HttpServletRequest request) {
        if (principal != null) {
            String username = principal.getName();
            userRepository.findByUsername(username).ifPresent(user -> {
                if (!user.getRoles().contains("ADMIN")) {
                    userService.deleteUserById(user.getId());
                    request.getSession().invalidate();
                }
            });
        }
        return "redirect:/profile";
    }

}

