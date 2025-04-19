package com.example.demo.controller.rest;

import com.example.demo.dto.CategoryDTO;
import com.example.demo.dto.NewUserRequestDTO;
import com.example.demo.dto.ProductDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.service.ProductService;
import com.example.demo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/api/users")
public class UserRESTController {
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ProductService productService;

    @GetMapping("/me")
    public UserDTO me(HttpServletRequest request) {

        Principal principal = request.getUserPrincipal();

        if(principal != null) {
            return userService.getUser(principal.getName());
        } else {
            throw new NoSuchElementException();
        }
    }
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody NewUserRequestDTO newUserRequest, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();

        // Si hay usuario autenticado, debe ser ADMIN
        if (principal != null) {
            UserDTO authUser = userService.findByUsername(principal.getName());
            if (!authUser.roles().contains("ADMIN")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Solo el administrador puede registrar usuarios");
            }
        }

        // Validar que no exista ya
        if (userService.existsByUsername(newUserRequest.username())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Ese nombre de usuario ya existe.");
        }

        // Crear siempre con rol USER
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


    /*@DeleteMapping("/{id}")
    public UserDTO deleteUser(@PathVariable long id) {
        return userService.deleteUserById(id);
    }*/

    @PostMapping("/cart/{productId}")
    public ResponseEntity<?> addproductToCart(@PathVariable Long productId, HttpServletRequest request){
        Principal principal = request.getUserPrincipal();
        if(principal == null){
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
    public ResponseEntity<?> addCartToOrder(HttpServletRequest request){
        Principal principal = request.getUserPrincipal();
        if(principal == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No hay usuario autenticado.");
        }
        try{
            UserDTO user = userService.productsFromCartIntoOrder2(principal.getName());
            return ResponseEntity.ok(user);
        } catch(Exception e) {
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

        // ✅ Un usuario normal solo puede eliminarse a sí mismo
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


}
