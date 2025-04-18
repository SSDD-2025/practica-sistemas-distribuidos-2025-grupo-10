package com.example.demo.controller.rest;

import com.example.demo.dto.CategoryDTO;
import com.example.demo.dto.ProductDTO;
import com.example.demo.dto.ProductMapper;
import com.example.demo.model.Category;
import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.ProductService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sound.sampled.Port;
import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
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
    @Autowired
    private UserService userService;

    /*
    @GetMapping("/api/products")
    public Collection<ProductDTO> getProducts() {
        return productService.findall();
    }

     */

    @GetMapping("/api/products/{id}")
    public ProductDTO getProduct(@PathVariable long id) {
        return productService.findProductById(id);
    }

    //Falta la gestión de las imágenes
    @PostMapping("/api/products")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO){
        productDTO = productService.createProduct(productDTO);
        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(productDTO.id()).toUri();
        return ResponseEntity.created(location).body(productDTO);
    }

    @PutMapping("/api/products/{id}")
    public ProductDTO updateProduct(@PathVariable long id, @RequestBody ProductDTO updatedProductDTO) throws SQLException, IOException {
        return productService.updateProduct(id, updatedProductDTO, null, false);
    }

    @DeleteMapping("/api/products/{id}")
    public ProductDTO deleteProduct(@PathVariable long id) {
        return productService.deleteProduct(id, userService);
    }
    private ProductDTO toDTO(Product product){
        return mapper.toDTO(product);
    }

    private Collection<ProductDTO> toDTOs(Collection<Product> product){
        return mapper.toDTOs(product);
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

    @GetMapping("/api/products")
    public ResponseEntity<?> getProducts(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {

        if(page != null && size != null) {
            Page<ProductDTO> productPage = productService.findPaginated(page, size);
            return ResponseEntity.ok(productPage);
        } else {
            return ResponseEntity.ok(productService.findall());
        }
    }

    @GetMapping("/api/products/short")
    public ResponseEntity<Page<ProductDTO>> getShortPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<ProductDTO> productPage = productService.findPaginated(page, size);
        return ResponseEntity.ok(productPage);
    }


}
