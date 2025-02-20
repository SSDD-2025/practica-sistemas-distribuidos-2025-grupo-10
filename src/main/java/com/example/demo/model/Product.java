package com.example.demo.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private BigDecimal price;

    //@ManyToOne
    //private Category category; //no se si está bien

    @ManyToOne
    @JoinColumn(name = "id", nullable = false) //  Un producto pertenece a una categoria,
                                                //  pero una categoria tiene varios productos
    private Category category;

    @ManyToMany(mappedBy = "Order")
    private List<Order> orders = new ArrayList<>();

    // Constructor vacío
    public Product() {
    }

    // Constructor con parámetros
    public Product(String name, BigDecimal price) {
        super();
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

    // Getters and setters for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter y setter para price
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    public List<Order> getOrders(){
        return orders;
    }
    public void setOrders(List<Order> orders){
        this.orders = orders;
    }
    public Category getCategory(){
        return category;
    }
    public void setCategory(Category category){
        this.category = category;
    }
}


