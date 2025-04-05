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
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryDTO){
        /*Category category = toDomain(categoryDTO);
        categoryRepository.save(category);
        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(category.getId()).toUri();
        return ResponseEntity.created(location).body(toDTO(category));*/
        categoryDTO = categoryService.addCategory(categoryDTO);
        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(categoryDTO.id()).toUri();
        return ResponseEntity.created(location).body(categoryDTO);
    }
    /*Not necessary because we dont update category
    @PutMapping("/api/categories/{id}")
    public CategoryDTO updateCategory(@PathVariable long id,
                                      @RequestBody CategoryDTO newCategoryDTO){
        if(categoryRepository.existsById(id)){
            Category newCategory = toDomain(newCategoryDTO);
            newCategory.setId(id);
            categoryRepository.save(newCategory);
            return toDTO(newCategory);
        }else{
            throw new NoSuchElementException();
        }
    }*/

    @DeleteMapping("/api/categories/{id}")
    public CategoryDTO deleteCategory(@PathVariable long id) {
        return categoryService.deleteCategoryById(id);
    }

}
