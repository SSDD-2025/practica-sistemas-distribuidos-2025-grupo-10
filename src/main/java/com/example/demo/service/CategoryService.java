package com.example.demo.service;

import com.example.demo.model.Category;
import com.example.demo.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public void save(Category category) {
        categoryRepository.save(category);
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public boolean addCategory(Category category) {
        if (!categoryRepository.findByName(category.getName()).isEmpty()) {
            return false; // Category already exists
        }
        categoryRepository.save(category);
        return true;
    }

    public Optional<Category> findCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    public void deleteCategoryById(Long id) {
        categoryRepository.deleteById(id);
    }

}
