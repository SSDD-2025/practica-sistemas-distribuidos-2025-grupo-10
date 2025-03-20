package com.example.demo.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String password;
    private String email;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles;

    //  Relations
    //  (Ejem 7)
    @OneToMany()
    @JoinColumn(name = "user_id")
    private List<Order> userOrders = new ArrayList<>();

    //  (Ejem 10) User - Product
    @ManyToMany
    private List<Product> userProducts = new ArrayList<>();

    public User() {

    }

    public User(String username, String password, String email, String... roles) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.roles = List.of(roles);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Order> getUserOrders() {
        return userOrders;
    }

    public void setUserOrders(List<Order> userOrders) {
        this.userOrders = userOrders;
    }

    public List<Product> getUserProducts() {
        return userProducts;
    }

    public void setUserProducts(List<Product> userProducts) {
        this.userProducts = userProducts;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
