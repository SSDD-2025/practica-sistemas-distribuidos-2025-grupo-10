package com.example.demo.repository;
import com.example.demo.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>{
    List<Product> findByName (String name);
    List<Product> findByCategory (Category categoryId);

}
