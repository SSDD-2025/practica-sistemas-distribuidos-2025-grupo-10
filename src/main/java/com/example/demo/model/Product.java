package com.example.demo.model;

public class Product {
    private String name;
    private int price;

    public Product(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }

    public int getprice() {
        return price;
    }

    public void setprice(int price) {
        this.price = price;
    }
}
