package com.example.demo.dto;

import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

public record NewProductRequestDTO(
        String name,
        BigDecimal price,
        MultipartFile imagefield,
        CategoryDTO category
) {
}
