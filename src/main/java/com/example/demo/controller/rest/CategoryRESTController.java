package com.example.demo.controller.rest;

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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collection;
import java.util.NoSuchElementException;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
public class CategoryRESTController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/api/categories")
    public Collection<CategoryDTO> getCategories() {
        return categoryService.findAll();
    }

    @GetMapping("/api/categories/{id}")
    public CategoryDTO getCategory(@PathVariable Long id) {
        return categoryService.findCategoryById(id);
    }
    @PostMapping("/api/categories")
    public ResponseEntity<?> createCategory(@RequestBody CategoryDTO categoryDTO){
        if (categoryDTO.id() != 0) {
            return ResponseEntity.badRequest().body("No se debe proporcionar un ID al crear una categoría");
        }
        categoryDTO = categoryService.addCategory(categoryDTO);
        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(categoryDTO.id()).toUri();
        return ResponseEntity.created(location).body(categoryDTO);
    }


    @DeleteMapping("/api/categories/{id}")
    public CategoryDTO deleteCategory(@PathVariable long id) {
        return categoryService.deleteCategoryById(id);
    }

}
