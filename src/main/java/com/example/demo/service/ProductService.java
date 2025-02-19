package com.example.demo.service;

import com.example.demo.model.Product;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
@Service
public class ProductService {
    private ConcurrentMap<Long, Product> products = new ConcurrentHashMap<>();
    private AtomicLong nextId = new AtomicLong();

    public ProductService(){
        save(new Product("Raul", new BigDecimal(48615)));
        save(new Product("AndresRex", new BigDecimal(4551)));
        save(new Product("Ruben Camacho", new BigDecimal(4515331)));
    }
    public void save(Product product){
        long id = nextId.getAndIncrement();
        product.setId(id);
        this.products.put(id, product);
    }
    public Collection<Product> findall(){
        return products.values();
    }
    public Product getProductById(Long id){
        return products.get(id);
    }
    public void deleteProductById(Long id){
        this.products.remove(id);
    }

}
