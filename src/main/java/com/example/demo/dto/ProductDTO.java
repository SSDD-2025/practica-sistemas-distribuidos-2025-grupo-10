package com.example.demo.dto;

import com.example.demo.model.Category;
import java.math.BigDecimal;
import java.util.List;

public record ProductDTO(
    Long id,
    String name,
    BigDecimal price,
    Category category
    //Blob imageFile,
    ) {

}