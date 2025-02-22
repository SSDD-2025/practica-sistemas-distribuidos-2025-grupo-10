package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    // Mostrar todos los usuarios
    @GetMapping("/users")
    public String showUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "users"; // vista que muestra la lista de usuarios
    }

    // Mostrar formulario para añadir un nuevo usuario
    @GetMapping("/users/add")
    public String showFormAdd(Model model) {
        model.addAttribute("user", new User()); // Crear un usuario vacío
        return "addUser"; // vista para agregar un nuevo usuario
    }

    // Agregar un nuevo usuario
    @PostMapping("/users/add")
    public String addUser(User user) {
        userService.save(user); // Guardamos el usuario
        return "redirect:/users"; // Redirigimos a la lista de usuarios
    }

    // Ver detalles de un usuario
    @GetMapping("/users/{id}")
    public String showUser(Model model, @PathVariable Long id) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "showUser"; // Vista que muestra los detalles del usuario
    }

    // Eliminar un usuario
    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id); // Eliminamos el usuario
        return "redirect:/users"; // Redirigimos a la lista de usuarios
    }
}
