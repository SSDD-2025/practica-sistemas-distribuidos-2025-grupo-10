package com.example.demo.dto;

import java.math.BigDecimal;
import java.util.Date;

public record OrderDTO(
        long id,
        BigDecimal total,
        int numItems,
        Date date,
        String status) {
}
