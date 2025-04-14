package com.example.demo.service;

import com.example.demo.dto.CategoryDTO;
import com.example.demo.dto.ProductDTO;
import com.example.demo.dto.ProductMapper;
import com.example.demo.dto.UserDTO;
import com.example.demo.model.Category;
import com.example.demo.model.Order;
import com.example.demo.model.Product;
import com.example.demo.model.User;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
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
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProductService {
    @Autowired
    private ProductMapper mapper;
    @Autowired
    private OrderRepository orderRepository;

    private final ProductRepository productRepository;
    private final OrderService orderService;
    @Autowired
    private CategoryRepository categoryRepository;
    private final CategoryService categoryService;

    public Collection<ProductDTO> findall() {
        return toDTOs(productRepository.findAll());
    }

    public ProductDTO findProductById(long id) {
        return toDTO(productRepository.findById(id).orElseThrow());
    }
    public Product findProductEntityById(long id) {
        return (productRepository.findById(id).orElseThrow());
    }
    /*
    public Collection<Product> findall() { //cambiar
        return productRepository.findAll();
    }
     */

    /*
    public Optional<Product> findProductById(Long id) { //cambiar al de abajo
        return productRepository.findById(id);
    }
    */


    public ProductService(ProductRepository productRepository, OrderService orderService, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.orderService = orderService;
        this.categoryService = categoryService;
    }

    // Se usa en product controller (ver cómo se podría cambiar para dejar de usar este save)
    public void save(Product product, Long id) {
        if (id != null) {
            Category category = categoryRepository.findById(id).orElseThrow();
            product.setCategory(category);
        } else {
            product.setCategory(null);
        }
        productRepository.save(product);
    }

    public void save(Product product, MultipartFile imageField, Long id) throws IOException {
        if (imageField != null && !imageField.isEmpty()) {
            product.setImageFile(BlobProxy.generateProxy(imageField.getInputStream(), imageField.getSize()));
            product.setImage(true);
        }
        this.save(product, id);
    }

    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = toDomain(productDTO);
        Category category = categoryRepository.findById(productDTO.category().id())
                .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada"));

        product.setCategory(category);
        productRepository.save(product);
        return toDTO(product);
    }

    public ProductDTO updateProduct(long id, ProductDTO productDTO) throws SQLException {
        Product oldProduct = productRepository.findById(id).orElseThrow();
        Product updatedProduct = toDomain(productDTO);
        updatedProduct.setId(id);
        if (oldProduct.isImage() && updatedProduct.isImage()) {
            updatedProduct.setImageFile(BlobProxy.generateProxy(oldProduct.getImageFile().getBinaryStream(),
                    oldProduct.getImageFile().length()));
        }
        productRepository.save(updatedProduct);
        return toDTO(updatedProduct);
    }

    public ProductDTO createOrReplaceProduct(long id, ProductDTO productDTO) throws SQLException {
        ProductDTO product;
        if (id == 0) {
            product = createProduct(productDTO);
        } else {
            product = updateProduct(id, productDTO);
        }
        return product;
    }

    public ProductDTO deleteProduct(long id, UserService userService) {
        Product product = productRepository.findById(id).orElseThrow();
        ProductDTO productDTO = toDTO(product);

        // Remove the product from all orders before deleting it
        for (Order order : product.getOrders()) {
            order.getProducts().remove(product);
            orderService.saveEntity(order);
        }

        // Lógica delegada a UserService
        userService.removeProductFromAllUsers(productDTO);

        // Deletes the product
        productRepository.deleteById(id);
        return productDTO;
    }



    //no funciona pero ya no da error
    public List<ProductDTO> getProductsByCategory(Long categoryId) {
        CategoryDTO categoryDTO = categoryService.findCategoryById(categoryId);
        if (categoryDTO != null) {
            List<Product> productsByCategory = productRepository.findCategoryById(categoryDTO.id());
            List<ProductDTO> productDTOS = (List<ProductDTO>) this.toDTOs(productsByCategory);
            return Collections.unmodifiableList(productDTOS);
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

    private ProductDTO toDTO(Product product) {
        return mapper.toDTO(product);
    }

    private Product toDomain(ProductDTO productDTO) {
        return mapper.toDomain(productDTO);
    }

    private Collection<ProductDTO> toDTOs(Collection<Product> product) {
        return mapper.toDTOs(product);
    }

}
