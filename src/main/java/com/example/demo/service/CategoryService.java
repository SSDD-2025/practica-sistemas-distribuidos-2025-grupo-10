package com.example.demo.service;

import com.example.demo.model.Category;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class CategoryService {

    private ConcurrentMap<Long, Category> categories = new ConcurrentHashMap<>();
    private AtomicLong nextId = new AtomicLong();

    public CategoryService(){
        save(new Category("Electronics"));
        save(new Category("Clothing"));
        save(new Category("Home & Kitchen"));
    }

    public void save(Category category){
        long id = nextId.getAndIncrement();
        category.setId(id);
        this.categories.put(id, category);
    }

    public Collection<Category> findAll(){
        return categories.values();
    }

    public Category getCategoryById(Long id){
        return categories.get(id);
    }

    public void deleteCategoryById(Long id){
        this.categories.remove(id);
    }
}

