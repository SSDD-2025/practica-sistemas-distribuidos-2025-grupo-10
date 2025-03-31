package com.example.demo.dto;

import com.example.demo.model.Product;
import org.mapstruct.Mapper;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductDTO toDTO(Product product);

    Product toDomain(ProductDTO productDTO);

    List<ProductDTO> toDTOs(Collection<Product> products);
}

