package com.example.demo.service;

import com.example.demo.model.Product;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ProductService {

    private ConcurrentHashMap<Long, Product> products = new ConcurrentHashMap<>();
    private AtomicLong nextId = new AtomicLong();

    public ProductService(){
        save();
    }
    public void save(){

    }

}
