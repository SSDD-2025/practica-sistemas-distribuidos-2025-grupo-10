package com.example.demo.controller;

import com.example.demo.dto.ProductDTO;
import com.example.demo.dto.ProductMapper;
import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
public class ProductRESTController {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductMapper mapper;

    @GetMapping("/api/products")
    public Collection<ProductDTO> getProduct() {
        return toDTOs(productRepository.findAll());
    }

    @GetMapping("/api/products/{id}")
    public ProductDTO getProduct(@PathVariable Long id) {
        return toDTO(productRepository.findById(id).orElseThrow());
    }

    @DeleteMapping("/api/products/{id}")
    public ProductDTO deleteProduct(@PathVariable long id) {
        Product product = productRepository.findById(id).orElseThrow();
        productRepository.deleteById(id);
        return toDTO(product);
    }
    private ProductDTO toDTO(Product product){
        return mapper.toDTO(product);
    }

    private Product toDomain(ProductDTO productDTO){
        return mapper.toDomain(productDTO);
    }

    private Collection<ProductDTO> toDTOs(Collection<Product> product){
        return mapper.toDTOs(product);
    }

}
