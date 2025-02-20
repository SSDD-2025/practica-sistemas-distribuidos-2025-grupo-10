package com.example.demo.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class Order {
    private long id;
    private BigDecimal total;
    private int numItems;
    private Date date;
    private User user;
    private String status;  // Estado del pedido (pendiente, procesado, etc.)

    public Order(long id, BigDecimal total, int numItems, Date date, User user, List<Product> products, String status) {
        this.id = id;
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
}
