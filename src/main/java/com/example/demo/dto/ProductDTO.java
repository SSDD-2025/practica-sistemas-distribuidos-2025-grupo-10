package com.example.demo.dto;

import com.example.demo.model.Category;
import java.math.BigDecimal;
import java.util.List;

public record ProductDTO(
    long id,
    String name,
    BigDecimal price,
    Boolean image,
    CategoryDTO category
    ) {

}