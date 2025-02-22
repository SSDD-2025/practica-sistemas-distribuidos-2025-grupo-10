package com.example.demo.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)   
    private long id;
    private BigDecimal total;
    private int numItems;
    private Date date;
    private User user; //creo que esto no debería ir ahí, tendrá un usuario asignado con lo de la BBDD
    private String status;  // Estado del pedido (pendiente, procesado, etc.)

    @ManyToMany(mappedBy = "orders")
    private List<Product> products = new ArrayList<>();

    public Order(){}
    public Order(BigDecimal total, int numItems, Date date, User user, List<Product> products, String status) {
        this.total = total;
        this.numItems = numItems;
        this.date = date;
        this.user = user;
        this.products = products;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public int getNumItems() {
        return numItems;
    }

    public void setNumItems(int numItems) {
        this.numItems = numItems;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public List<Product> getProducts(){
        return products;
    }
    public void setProducts(List<Product> products){
        this.products = products;
    }
}
