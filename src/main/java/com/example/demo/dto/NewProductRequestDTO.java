package com.example.demo.dto;

import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.nio.channels.MulticastChannel;

public record NewProductRequestDTO (
        String name,
        BigDecimal price,
        MultipartFile imagefield,
        CategoryDTO category
){
}
