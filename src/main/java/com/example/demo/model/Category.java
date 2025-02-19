package com.example.demo.model;

import java.util.HashSet;
import java.util.Set;

public class Category {
    private Long id;
    private String name;
    private Set<Product> products = new HashSet<>();  // Conjunto de productos asociados a la categoría

    public Category(String name) {
        this.name = name;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void addProduct(Product product) {
        this.products.add(product);  // Agrega un producto a la categoría
    }
}

