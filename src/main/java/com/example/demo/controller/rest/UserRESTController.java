package com.example.demo.controller.rest;

import com.example.demo.dto.NewUserRequestDTO;
import com.example.demo.dto.OrderDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/users")
public class UserRESTController {
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @GetMapping("/me")
    public UserDTO me(HttpServletRequest request) {

        Principal principal = request.getUserPrincipal();

        if (principal != null) {
            return userService.getUser(principal.getName());
        } else {
            throw new NoSuchElementException();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody NewUserRequestDTO newUserRequest, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();

        // If there's an authenticated user, it should be an admin
        if (principal != null) {
            UserDTO authUser = userService.findByUsername(principal.getName());
            if (!authUser.roles().contains("ADMIN")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Solo el administrador puede registrar usuarios");
            }
        }

        // Validate if it already exists
        if (userService.existsByUsername(newUserRequest.username())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Ese nombre de usuario ya existe.");
        }

        // Create always with the role USER
        NewUserRequestDTO newUser = new NewUserRequestDTO(
                newUserRequest.username(),
                newUserRequest.email(),
                List.of("USER"),
                passwordEncoder.encode(newUserRequest.password())
        );

        UserDTO createdUser = userService.createNewUser(newUser);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdUser);
    }

    @PostMapping("/cart/{productId}")
    public ResponseEntity<?> addproductToCart(@PathVariable Long productId, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No hay usuario autenticado.");
        }
        try {
            UserDTO user = userService.addProductToCart2(productId, principal.getName());
            return ResponseEntity.ok(user); //productService.findProductById(productId)
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al añadir el producto al carrito: " + e.getMessage());
        }
    }

    @PostMapping("/cartToOrder")
    public ResponseEntity<?> addCartToOrder(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No hay usuario autenticado.");
        }
        try {
            UserDTO user = userService.productsFromCartIntoOrder2(principal.getName());
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al añadir el producto al carrito: " + e.getMessage());
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable long id, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();

        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no autenticado");
        }

        String username = principal.getName();
        UserDTO authUser = userService.findByUsername(username);
        UserDTO targetUser;

        try {
            targetUser = userService.getUserById(id);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }

        if (authUser.id().equals(targetUser.id()) && authUser.roles().contains("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Un administrador no puede eliminar su propia cuenta");
        }

        // A user only can erase itself
        if (!authUser.roles().contains("ADMIN") && !authUser.id().equals(targetUser.id())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No puedes eliminar a otro usuario");
        }

        userService.deleteUserById(id);
        return ResponseEntity.ok("Usuario eliminado correctamente");
    }


    @GetMapping
    public ResponseEntity<?> getAllUsers(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();

        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no autenticado");
        }

        UserDTO authUser = userService.findByUsername(principal.getName());

        if (!authUser.roles().contains("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado: solo para administradores");
        }

        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/me/myOrders")
    public ResponseEntity<?> getMyOrders(HttpServletRequest request) {

        Principal principal = request.getUserPrincipal();

        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no autenticado");
        }

        UserDTO authUser = userService.findByUsername(principal.getName());


        return ResponseEntity.ok(authUser.userOrders());
    }
    @GetMapping("/me/myCart")
    public ResponseEntity<?> getMyCart(HttpServletRequest request) {

        Principal principal = request.getUserPrincipal();

        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no autenticado");
        }

        UserDTO authUser = userService.findByUsername(principal.getName());


        return ResponseEntity.ok(authUser.userProducts());
    }
    @DeleteMapping("/me/myOrders")
    public ResponseEntity<?> deleteMyOrders(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();

        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no autenticado");
        }

        UserDTO authUser = userService.findByUsername(principal.getName());

        List<OrderDTO> ordersDeleted = authUser.userOrders();

        userService.deleteOrder2(authUser);

        return ResponseEntity.ok(ordersDeleted);
    }



}
