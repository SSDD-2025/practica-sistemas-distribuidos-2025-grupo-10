package com.example.demo.dto;

import com.example.demo.model.Category;
import org.mapstruct.Mapper;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDTO toDTO(Category category);
    List<CategoryDTO> toDTOs(Collection<Category> categories);
    Category toDomain(CategoryDTO categoryDTO);

}
