package com.example.demo.service;

import com.example.demo.model.Product;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ProductService {
    private ConcurrentHashMap<Long, Product> products = new ConcurrentHashMap<>();
    private AtomicLong nextId = new AtomicLong();

    public ProductService(){
        save(new Product("Raul", 1234));
        save(new Product("AndresRex", 1234));
        save(new Product("Ruben Camacho", 1234));
    }
    public Product save(Product productToInsert){
        long l = nextId.incrementAndGet();
        products.put(l, productToInsert);
        return productToInsert;
    }

}
