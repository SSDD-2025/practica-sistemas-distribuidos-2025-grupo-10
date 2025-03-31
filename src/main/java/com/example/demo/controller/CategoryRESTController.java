package com.example.demo.controller;

import com.example.demo.dto.CategoryDTO;
import com.example.demo.dto.CategoryMapper;
import com.example.demo.dto.OrderDTO;
import com.example.demo.dto.OrderMapper;
import com.example.demo.model.Category;
import com.example.demo.model.Order;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
public class CategoryRESTController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryMapper mapper;

    @GetMapping("/api/categories")
    public Collection<CategoryDTO> getCategory() {
        return toDTOs(categoryRepository.findAll());
    }

    @GetMapping("/api/categories/{id}")
    public CategoryDTO getCategory(@PathVariable Long id) {
        return toDTO(categoryRepository.findById(id).orElseThrow());
    }


    @DeleteMapping("/api/categories/{id}")
    public CategoryDTO deleteCategory(@PathVariable long id) {
        Category category = categoryRepository.findById(id).orElseThrow();
        categoryRepository.deleteById(id);
        return toDTO(category);
    }
    private CategoryDTO toDTO(Category category){
        return mapper.toDTO(category);
    }

    private Category toDomain(CategoryDTO categoryDTO){
        return mapper.toDomain(categoryDTO);
    }

    private Collection<CategoryDTO> toDTOs(Collection<Category> categories){
        return mapper.toDTOs(categories);
    }

}
