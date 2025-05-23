package com.example.demo.controller.web;

import com.example.demo.dto.UserDTO;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/add")
    public String showAddUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("users", userService.findAll());
        return "addUser";
    }

    @PostMapping("/add")
    public String addUser(@ModelAttribute UserDTO user) {
        userService.save(user);
        return "redirect:/users/add";
    }

    @GetMapping("/users/add")
    public String showFormAdd(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("users", userService.findAll());
        return "addUser";
    }

    @GetMapping("/users/{id}")
    public String showUser(Model model, @PathVariable Long id) {
        Optional<User> user = userService.findUserById(id);
        if (user.isPresent()) {
            model.addAttribute("order", user.get());
            return "order";
        } else {
            return "orders";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return "redirect:/users/manage";
    }

    @GetMapping("/manage")
    public String showManageUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "deleteUsers";
    }

}
