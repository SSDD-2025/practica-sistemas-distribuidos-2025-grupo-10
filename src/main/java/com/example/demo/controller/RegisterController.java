package com.example.demo.controller;

import com.example.demo.dto.NewUserRequestDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class RegisterController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String showRegisterForm(Model model, CsrfToken token) {
        model.addAttribute("user", new User());
        model.addAttribute("token", token.getToken());
        return "register";
    }


    @PostMapping("/register")
    public String processRegistration(@ModelAttribute User user, Model model) {
        if (userService.existsByUsername(user.getUsername())) {
            model.addAttribute("error", "Ese nombre de usuario ya existe.");
            return "register";
        }

        NewUserRequestDTO newUser = new NewUserRequestDTO(
                user.getUsername(),
                user.getEmail(),
                List.of("USER"),
                passwordEncoder.encode(user.getPassword())
        );

        userService.createNewUser(newUser);

        return "redirect:/login?registered";
    }

}

