package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private String status;  // Estado del pedido (pendiente, procesado, etc.)

    //  Relations
    //  Verificada (Team)
    @ManyToMany(mappedBy = "orders")
    private List<Product> products;

    //  Verificada (Ejem 8)
    @ManyToOne
    private User user;

    public Order(){

    }

    public Order(BigDecimal total, int numItems, Date date, List<Product> products, String status) {
        this.total = total;
        this.numItems = numItems;
        this.date = date;
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
