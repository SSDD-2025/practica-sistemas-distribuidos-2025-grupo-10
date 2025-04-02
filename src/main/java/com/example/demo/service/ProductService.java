package com.example.demo.service;

import com.example.demo.dto.ProductBasicDTO;
import com.example.demo.dto.ProductDTO;
import com.example.demo.dto.ProductMapper;
import com.example.demo.model.Category;
import com.example.demo.model.Order;
import com.example.demo.model.Product;
import com.example.demo.model.User;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.hibernate.annotations.processing.SQL;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductMapper mapper;
    @Autowired
    private OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderService orderService;
    private final UserService userService;
    private final CategoryService categoryService;

    public ProductService(ProductRepository productRepository, OrderService orderService, UserService userService, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.orderService = orderService;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    public ProductDTO save(ProductDTO productDTO) {
        if(productDTO.id() != null){
            throw new IllegalArgumentException();
        }
        Product product = toDomain(productDTO);
        productRepository.save(product);
        // No se si es necesario en nuestro caso. Puede que sea usuario y no order
        product.getOrders().replaceAll(order -> orderRepository.findById(order.getId()).orElseThrow());
        return toDTO(product);
    }
    /**
    public void save(ProductBasicDTO productDTO, MultipartFile imageField) throws IOException {
        if (imageField != null && !imageField.isEmpty()) {
            productDTO.setImageFile(BlobProxy.generateProxy(imageField.getInputStream(), imageField.getSize()));
        }
        productRepository.save(product);
    }
     **/

    public Collection<ProductDTO> findall() {
        return toDTOs(productRepository.findAll());
    }

    public ProductDTO findProductById(Long id) {
        return toDTO(productRepository.findById(id).orElseThrow());
    }

    public ProductDTO updateProduct(long id, ProductDTO updatedProductDTO) throws SQLException {
        Product oldProduct = productRepository.findById(id).orElseThrow();
        Product updatedProduct = toDomain(updatedProductDTO);
        updatedProduct.setId(id);
        if(oldProduct.isImage() && updatedProduct.isImage()){
            updatedProduct.setImageFile(BlobProxy.generateProxy(oldProduct.getImageFile().getBinaryStream(),
                    oldProduct.getImageFile().length()));
        }
        productRepository.save(updatedProduct);
        return toDTO(updatedProduct);

    }


    public ProductDTO deleteProduct(long id) {
        Product product = productRepository.findById(id).orElseThrow();

        // Remove the product from all orders before deleting it
        for (Order order : product.getOrders()) {
            order.getProducts().remove(product);
            orderService.save(order); // Guardar el pedido sin el producto
        }

        for (User u : userService.findAll()) {
            if (u.getUserProducts().contains(product)) {
                u.getUserProducts().remove(product);
                userService.save(u);
            }
        }
        ProductDTO productDTO = toDTO(product);
        // Deletes the product
        productRepository.deleteById(id);
        return productDTO;
    }

    public List<Product> getProductsByCategory(Long categoryId) {
        Optional<Category> category = categoryService.findCategoryById(categoryId);
        if (category.isPresent()) {
            return productRepository.findCategoryById(category.get().getId());
        } else {
            return List.of();
        }
    }

    public Resource getProductImage(long id) throws SQLException {

        Product product = productRepository.findById(id).orElseThrow();

        if (product.getImageFile() != null) {
            return new InputStreamResource(product.getImageFile().getBinaryStream());
        } else {
            throw new NoSuchElementException();
        }
    }
    public void createProductImage(long id, InputStream inputStream, long size) {

        Product product = productRepository.findById(id).orElseThrow();

        product.setImage(true);
        product.setImageFile(BlobProxy.generateProxy(inputStream, size));

        productRepository.save(product);
    }
    public void updateProductImage(long id, InputStream inputStream, long size) {

        Product product = productRepository.findById(id).orElseThrow();

        if (!product.isImage()) {
            throw new NoSuchElementException();
        }

        product.setImageFile(BlobProxy.generateProxy(inputStream, size));

        productRepository.save(product);
    }
    public void deleteProductImage(long id) {

        Product product = productRepository.findById(id).orElseThrow();

        if (!product.isImage()) {
            throw new NoSuchElementException();
        }

        product.setImageFile(null);
        product.setImage(false);

        productRepository.save(product);
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
