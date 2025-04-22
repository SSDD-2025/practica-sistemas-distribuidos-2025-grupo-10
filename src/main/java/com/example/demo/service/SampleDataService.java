package com.example.demo.service;

import com.example.demo.model.Category;
import com.example.demo.model.Product;
import com.example.demo.model.User;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

@Service
public class SampleDataService {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryRepository categoryRepository;
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
    @Autowired
    private OrderRepository orderRepository;


    @PostConstruct
    public void init() throws IOException {

        //  Initialize categories
        categoryRepository.save(new Category("Discos"));
        categoryRepository.save(new Category("Libros"));
        List<Category> all = categoryService.findAllEntities();
        Category category = all.get(0);
        Category category2 = all.get(1);

        //  Initialize products
        Product product1 = new Product("Producto Ejemplo 1", new BigDecimal("15.99"));
        Product product2 = new Product("Producto Ejemplo 2", new BigDecimal("7.50"));
        productService.save(product1, category.getId());
        productService.save(product2, category2.getId());


        int x = 50; // Number of products to create
        Random random = new Random();

        for (int i = 1; i <= x; i++) {
            String productName = "Producto Ejemplo " + i;
            BigDecimal price = BigDecimal.valueOf(5 + (100 - 5) * random.nextDouble()).setScale(2, BigDecimal.ROUND_HALF_UP);

            Product product = new Product(productName, price);

            Long categoryId = (i % 2 == 0) ? category.getId() : category2.getId();

            productService.save(product, categoryId);
        }

        // Initialize user
        userRepository.save(new User("vero", passwordEncoder.encode("cont"), "vero@gmail.com", "USER"));
        userRepository.save(new User("user", passwordEncoder.encode("pass"), "user@gmail.com", "USER"));

        //Load admin from properties file
        if (userRepository.findByUsername(adminUsername).isEmpty()) {
            userRepository.save(new User(adminUsername, adminPassword, "admin@email.com", "ADMIN"));
        }
        userRepository.save(new User("user2", passwordEncoder.encode("pass2"), "user2@gmail.com", "USER"));


    }

    private void saveproductWithURLImage(Product product, String image) throws IOException {
        product.setImageFile(imageUtils.localImageToBlob("images/" + image));
        productService.save(product, null);
    }
}
