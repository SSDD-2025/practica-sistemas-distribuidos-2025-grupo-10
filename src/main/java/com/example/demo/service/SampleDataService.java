package com.example.demo.service;

import com.example.demo.model.Category;
import com.example.demo.model.Order;
import com.example.demo.model.Product;
import com.example.demo.model.User;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
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

    @PostConstruct
    public void init() {


        //  Inicializar categoria
        categoryService.save(new Category("Categoria de cositas XD"));
        //  Inicializar un producto1 a categoria 1
        List<Category> all = categoryService.findAll();
        Category category = all.get(0);
        Product product1 = new Product("producto numero 1", new BigDecimal(15));
        product1.setCategory(category);


        //  Iniciar un usuario
        User user = new User("Alberto", "password", "Hola");

        //  Inicializar un pedido que tenga un producto1
        Order order1 = new Order(new BigDecimal(15), 15, new Date(), null, "En proceso");
        product1.getOrders().add(order1);


        //  No importa
        userService.save(new User());
        //  Fin no importa

        //  Inicializar el usuario con un pedido

        user.getUserOrders().add(order1);
        //user.getUserProducts().add(product1);
        userService.save(user);
        product1.getUsers().add(user);
        productService.save(product1);

    }
}
