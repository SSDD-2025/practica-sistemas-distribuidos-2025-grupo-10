package com.example.demo.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public record OrderDTO(
        Long id,
        BigDecimal total,
        int numItems,
        Date date,
        String status,
        List<ProductDTO> products
) {
}
