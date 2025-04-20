package com.example.demo.dto;

import java.math.BigDecimal;

public record ProductDTO(
        long id,
        String name,
        BigDecimal price,
        Boolean image,
        CategoryDTO category
) {

}