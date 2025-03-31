package com.example.demo.dto;

import com.example.demo.model.Category;
import java.math.BigDecimal;


public class ProductDTO {
    private Long id;
    private String name;
    private BigDecimal price;
    private String categoryName;
    private Category category;
}