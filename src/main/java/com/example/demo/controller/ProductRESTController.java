package com.example.demo.controller;

import com.example.demo.dto.ProductBasicDTO;
import com.example.demo.dto.ProductDTO;
import com.example.demo.dto.ProductMapper;
import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sound.sampled.Port;
import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.util.Collection;
import java.util.NoSuchElementException;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
public class ProductRESTController {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductMapper mapper;

    @GetMapping("/api/products")
    public Collection<ProductDTO> getProducts() {
        return productService.findall();
    }

    @GetMapping("/api/products/{id}")
    public ProductDTO getProduct(@PathVariable Long id) {
        return productService.findProductById(id);
    }

    //Falta la gestión de las imágenes
    @PostMapping("/api/products")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO){
        productDTO = productService.save(productDTO);
        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(productDTO.id()).toUri();
        return ResponseEntity.created(location).body(productDTO);
    }
    @PutMapping("/api/products/{id}")
    public ProductDTO updateProduct(@PathVariable long id, @RequestBody ProductDTO newProductDTO) throws SQLException {
        return productService.updateProduct(id, newProductDTO);
    }

    @DeleteMapping("/api/products/{id}")
    public ProductDTO deleteProduct(@PathVariable long id) {
        return productService.deleteProduct(id);
    }

    @PostMapping("/api/products/{id}/image")
    public ResponseEntity<Object> createProductImage(@PathVariable long id, @RequestParam MultipartFile imageFile) throws IOException {
        productService.createProductImage(id, imageFile.getInputStream(), imageFile.getSize());
        URI location = fromCurrentRequest().build().toUri();
        return ResponseEntity.created(location).build();
    }
    @GetMapping("/api/products/{id}/image")
    public ResponseEntity<Object> getProductImage(@PathVariable long id) throws SQLException, IOException {

        Resource productImage = productService.getProductImage(id);

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                .body(productImage);

    }
    @PutMapping("/api/products/{id}/image")
    public ResponseEntity<Object> updateProductImage(@PathVariable long id, @RequestParam MultipartFile imageFile)
            throws IOException {
        productService.updateProductImage(id, imageFile.getInputStream(), imageFile.getSize());
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/api/products/{id}/image")
    public ResponseEntity<Object> deleteProductImage(@PathVariable long id) throws IOException {
        productService.deleteProductImage(id);
        return ResponseEntity.noContent().build();
    }

}
