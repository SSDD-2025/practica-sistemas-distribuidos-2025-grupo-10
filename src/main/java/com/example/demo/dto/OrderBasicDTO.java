package com.example.demo.dto;

import java.math.BigDecimal;
import java.util.Date;

// For orders without products
public record OrderBasicDTO(
        long id,
        BigDecimal total,
        int numItems,
        Date date,
        String status
) {
}
