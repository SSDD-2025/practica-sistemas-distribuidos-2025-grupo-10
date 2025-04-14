package com.example.demo.dto;

import java.util.List;

public record NewUserRequestDTO(
        String username,
        String email,
        List<String> roles,
        String password
) {}
