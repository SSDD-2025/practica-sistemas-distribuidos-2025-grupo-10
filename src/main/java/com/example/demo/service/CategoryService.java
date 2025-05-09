package com.example.demo.service;

import com.example.demo.dto.CategoryDTO;
import com.example.demo.dto.CategoryMapper;
import com.example.demo.model.Category;
import com.example.demo.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryMapper mapper;

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public void save(Category category) {
        categoryRepository.save(category);
    }

    public Collection<CategoryDTO> findAll() {
        return toDTOs(categoryRepository.findAll());
    }

    List<Category> findAllEntities() {
        return categoryRepository.findAll();
    }


    public CategoryDTO addCategory(CategoryDTO categoryDTO) {
        if (!categoryRepository.findByName(categoryDTO.name()).isEmpty()) {
            throw new IllegalArgumentException("Ya existe una categoría con ese nombre");
        }

        Category category = toDomain(categoryDTO);
        categoryRepository.save(category);
        return toDTO(category);
    }


    public CategoryDTO findCategoryById(Long id) {
        return toDTO(categoryRepository.findById(id).orElseThrow());
    }


    public CategoryDTO deleteCategoryById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow();
        categoryRepository.deleteById(id);
        return toDTO(category);
    }

    //Mapper methods
    private CategoryDTO toDTO(Category category) {
        return mapper.toDTO(category);
    }

    private Category toDomain(CategoryDTO categoryDTO) {
        return mapper.toDomain(categoryDTO);
    }

    private Collection<CategoryDTO> toDTOs(Collection<Category> categories) {
        return mapper.toDTOs(categories);
    }

}
