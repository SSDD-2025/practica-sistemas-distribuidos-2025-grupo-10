package com.example.demo.controller.rest;

import com.example.demo.dto.ProductDTO;
import com.example.demo.dto.ProductMapper;
import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.sound.sampled.Port;
import java.net.URI;
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
        return toDTOs(productRepository.findAll());
    }

    @GetMapping("/api/products/{id}")
    public ProductDTO getProduct(@PathVariable Long id) {
        return toDTO(productRepository.findById(id).orElseThrow());
    }

    //Falta la gestión de las imágenes
    @PostMapping("/api/products")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO){
        Product product = toDomain(productDTO);
        productRepository.save(product);
        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(product.getId()).toUri();
        return ResponseEntity.created(location).body(toDTO(product));
    }
    @PutMapping("/api/products/{id}")
    public ProductDTO updateProduct(@PathVariable long id,
                                    @RequestBody ProductDTO newProductDTO){
        if(productRepository.existsById(id)){
            Product newProduct = toDomain(newProductDTO);
            newProduct.setId(id);
            productRepository.save(newProduct);
            return toDTO(newProduct);
        }else{
            throw new NoSuchElementException();
        }
    }

    @DeleteMapping("/api/products/{id}")
    public ProductDTO deleteProduct(@PathVariable long id) {
        return productService.deleteProduct(id);
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
