package com.example.demo.dto;

import com.example.demo.model.Category;
import com.example.demo.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductDTO toDTO(Product product);

    List<ProductDTO> toDTOs(Collection<Product> products);

    @Mapping(target = "imageFile", ignore = true)
    Product toDomain(ProductDTO productDTO);

    @Mapping(target = "products", ignore = true)
    Category toDomain(CategoryDTO categoryDTO);
}

