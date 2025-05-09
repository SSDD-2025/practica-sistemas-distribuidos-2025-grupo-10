package com.example.demo.model;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private BigDecimal price;
    @Lob
    private Blob imageFile;
    private boolean image;

    //  Relations
    //  (ejem 9)
    @ManyToOne
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Category category;

    //  (ejem 10) Order - Product
    @ManyToMany(mappedBy = "products")
    private List<Order> orders = new ArrayList<>();

    //  (ejem 10) User - Product
    @ManyToMany(mappedBy = "userProducts")
    private List<User> users = new ArrayList<>();

    public Product() {

    }

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

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public String getCategoryName() {
        return (category != null) ? category.getName() : "It has no category";
    }

    public Blob getImageFile() {
        return imageFile;
    }

    public void setImageFile(Blob imageFile) {
        this.imageFile = imageFile;
    }

    public boolean isImage() {
        return image;
    }

    public void setImage(boolean image) {
        this.image = image;
    }
}


