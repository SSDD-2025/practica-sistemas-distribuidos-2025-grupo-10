package com.example.demo.service;

import com.example.demo.model.Category;
import com.example.demo.model.Order;
import com.example.demo.model.Product;
import com.example.demo.model.User;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final OrderService orderService;
    private final UserService userService;
    private CategoryRepository categoryRepository;
    private final CategoryService categoryService;

    public ProductService(ProductRepository productRepository, OrderService orderService, UserService userService, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.orderService = orderService;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    public void save(Product product, Long id) {
        if (id != null) {
            Category category = categoryRepository.findById(id).orElseThrow();
            product.setCategory(category);
        } else {
            product.setCategory(null);
        }
        productRepository.save(product);
    }

    public void save(Product product, MultipartFile imageField, long id) throws IOException {
        if (imageField != null && !imageField.isEmpty()) {
            product.setImageFile(BlobProxy.generateProxy(imageField.getInputStream(), imageField.getSize()));
        }
        this.save(product, id);
    }

    public Collection<Product> findall() {
        return productRepository.findAll();
    }

    public Optional<Product> findProductById(Long id) {
        return productRepository.findById(id);
    }

    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
    }

    public void updateProduct(Product oldProduct, Product updatedProduct) {
        oldProduct.setName(updatedProduct.getName());
        oldProduct.setPrice(updatedProduct.getPrice());
        oldProduct.setCategory(updatedProduct.getCategory());
        productRepository.save(oldProduct);
    }

    @Transactional
    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

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

        // Deletes the product
        productRepository.delete(product);
    }

    /*

    public List<Product> getProductsByCategory(Long categoryId) {
        Optional<Category> category = categoryService.findCategoryById(categoryId);
        if (category.isPresent()) {
            return productRepository.findCategoryById(category.get().getId());
        } else {
            return List.of();
        }
    }*/
}
