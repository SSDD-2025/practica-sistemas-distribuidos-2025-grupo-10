package com.example.demo.dto;

import java.util.List;

// For users without products
public record UserBasicDTO(
        Long id,
        String username,
        String email,
        List<String> roles
) {
}
