package com.example.demo.controller;

import com.example.demo.model.Order;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/add")
    public String showAddUserForm() {
        return "addUser"; // Esto busca addUser.html en src/main/resources/templates
    }

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
        Optional<User> user = userService.findUserById(id);
        if (user.isPresent()){
            model.addAttribute("order", user.get());
            return "order";
        } else{
            return "orders";
        }

    }

    // Eliminar un usuario
    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        Optional<User> user = userService.findUserById(id);
        if (user.isPresent()){
            userService.deleteUserById(id);
            return "order";
        } else{
            return "orders";
        }
    }
}
