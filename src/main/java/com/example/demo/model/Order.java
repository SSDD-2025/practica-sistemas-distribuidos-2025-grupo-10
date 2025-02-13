package com.example.demo.model;

import java.util.Date;

public class Order {
    private int id;
    private int total;
    private int numItems;
    private Date date;
    private User user;

    public Order(int id, int total, int numItems, Date date, User user) {
        this.id = id;
        this.total = total;
        this.numItems = numItems;
        this.date = date;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getNumItems() {
        return numItems;
    }

    public void setNumItems(int numItems) {
        this.numItems = numItems;
    }

    public Date getdate() {
        return date;
    }

    public void setdate(Date date) {
        this.date = date;
    }

    public User getuser() {
        return user;
    }

    public void setuser(User user) {
        this.user = user;
    }
}
