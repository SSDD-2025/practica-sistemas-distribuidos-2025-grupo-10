package com.example.demo.dto;

import com.example.demo.model.Category;

import java.math.BigDecimal;

// For products without users or orders
public record ProductBasicDTO(
        Long id,
        String name,
        BigDecimal price,
        String categoryName,
        Category category
) {
}
