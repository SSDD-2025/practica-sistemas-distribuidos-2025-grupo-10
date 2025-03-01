package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private BigDecimal price;

    //  Relations
    //  (ejem 9)
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = true)
    @OnDelete(action = OnDeleteAction.SET_NULL) //  Si un producto no tiene categoria, quiere que en el html ponga "NO tiene categoria"
    private Category category;

    //  (ejem 10) Order - Product
    @ManyToMany(mappedBy = "products")
    private List<Order> orders = new ArrayList<>();

    //  (ejem 10) User - Product
    @ManyToMany(mappedBy="userProducts")
    private List<User> users = new ArrayList<>();

    // Constructor vacío
    public Product() {

    }

    // Constructor con parámetros
    public Product(String name, BigDecimal price) {
        super();
        this.name = name;
        this.price = price;
    }

    //  Getter y setters
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
    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}


