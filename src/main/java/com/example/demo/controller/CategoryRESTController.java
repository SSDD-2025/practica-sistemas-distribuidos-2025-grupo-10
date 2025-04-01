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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    private CategoryMapper mapper;

    @GetMapping("/api/categories")
    public Collection<CategoryDTO> getCategories() {
        return toDTOs(categoryRepository.findAll());
    }

    @GetMapping("/api/categories/{id}")
    public CategoryDTO getCategory(@PathVariable Long id) {
        return toDTO(categoryRepository.findById(id).orElseThrow());
    }
    @PostMapping("/api/categories")
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryDTO){
        Category category = toDomain(categoryDTO);
        categoryRepository.save(category);
        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(category.getId()).toUri();
        return ResponseEntity.created(location).body(toDTO(category));
    }
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
