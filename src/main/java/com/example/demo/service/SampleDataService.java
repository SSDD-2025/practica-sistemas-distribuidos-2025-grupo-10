package com.example.demo.service;

import com.example.demo.model.Category;
import com.example.demo.model.Product;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Service
public class SampleDataService {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;
    @Autowired
    private ImageUtils imageUtils;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.password}")
    private String adminPassword;


    @PostConstruct
    public void init() throws IOException {

        //  Initialize categories
        categoryService.save(new Category("Discos"));
        categoryService.save(new Category("Libros"));
        List<Category> all = categoryService.findAllEntities();
        Category category = all.get(0);
        Category category2 = all.get(1);

        //  Initialize products
        Product product1 = new Product("Producto Ejemplo 1", new BigDecimal("15.99"));
        Product product2 = new Product("Producto Ejemplo 2", new BigDecimal("7.50"));
        product1.setCategory(category);
        product2.setCategory(category2);
        productService.save(product1, null);
        productService.save(product2, null);

        // Initialize user
        //User user = new User("Alberto", "ItsNotPassword", "alberto123@gmail.com");
        //userService.save(user);
        userRepository.save(new User("vero", passwordEncoder.encode("adminpass"), "vero@gamil.com", "USER", "ADMIN"));
        userRepository.save(new User("user", passwordEncoder.encode("pass"), "vero@gamil.com", "USER"));

        //Load admin from properties file
        if (userRepository.findByUsername(adminUsername).isEmpty()) {
            userRepository.save(new User(adminUsername, adminPassword, "admin@email.com", "ADMIN"));
        }

    }

    private void saveproductWithURLImage(Product product, String image) throws IOException {
        product.setImageFile(imageUtils.localImageToBlob("images/" + image));
        productService.save(product, null);
    }
}
