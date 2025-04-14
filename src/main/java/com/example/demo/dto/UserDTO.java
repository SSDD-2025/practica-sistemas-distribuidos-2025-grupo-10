package com.example.demo.dto;

import com.example.demo.model.Order;
import com.example.demo.model.Product;

import java.util.List;

public record UserDTO(
        Long id,
        String username,
        String email,
        List<String> roles,
        List<ProductDTO> userProducts,
        List<Order> userOrders) {
}
