package com.example.demo.repository;
import com.example.demo.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>{
    List<Product> findByName (String name);
    @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId")
    List<Product> findCategoryById(@Param("categoryId") Long categoryId);

}
