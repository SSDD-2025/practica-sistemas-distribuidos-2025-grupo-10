package com.example.demo.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public record OrderDTO(
        long id,
        BigDecimal total,
        int numItems,
        Date date,
        String status,
        List<ProductBasicDTO> products
) {
}
