package com.example.demo.model;

public class Product {
    private Long id;   // El ID ahora es Long, para que coincida con los demás modelos.
    private String name;
    private int price;

    // Constructor vacío
    public Product() {
    }

    // Constructor con parámetros
    public Product(String name, int price) {
        this.name = name;
        this.price = price;
    }

    // Getter y setter para id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Getter y setter para name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter y setter para price
    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}

