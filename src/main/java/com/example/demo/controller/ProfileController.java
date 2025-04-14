package com.example.demo.controller;

import com.example.demo.dto.UserDTO;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String showProfile(Model model, Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            UserDTO userDTO = userService.findByUsername(username);
            model.addAttribute("user", userDTO);
            return "profile";
            /*
            if (user != null) {
                model.addAttribute("user", user);
                return "profile";
            }
             */
        }
        return "redirect:/login";
    }

    @PostMapping("/delete")
    public String deleteAccount(Principal principal, HttpServletRequest request) {
        if (principal != null) {
            String username = principal.getName();
            try{
                UserDTO byUsername = userService.findByUsername(username);
                if (!byUsername.roles().contains("ADMIN")) {
                    userService.deleteUserById(byUsername.id());
                    request.getSession().invalidate();
                }
            } catch (NoSuchElementException e) {
                return "error";
            }

        }
        return "redirect:/profile";

    }

}

