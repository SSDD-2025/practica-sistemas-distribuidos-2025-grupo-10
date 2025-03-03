package com.example.demo.service;

import com.example.demo.model.Order;
import com.example.demo.model.Product;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class UserService {

    @Autowired
    private OrderService orderService;

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public Collection<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    public void addProductToCart(Product productToSave, User user) {
        List<Product> userProducts = user.getUserProducts();
        ArrayList<Product> products = new ArrayList<>(userProducts);
        products.add(productToSave);
        user.setUserProducts(products);
        this.save(user);
    }

    public void productsFromCartIntoOrder(User user) {
        List<Product> allCartProducts = new ArrayList<>(user.getUserProducts());
        user.getUserProducts().clear();
        this.save(user);
        //  Add all products from cart into an order
        BigDecimal total = allCartProducts.stream()
                .map(Product::getPrice)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        Order order = new Order();
        order.getProducts().addAll(allCartProducts);
        order.setTotal(total);
        order.setNumItems(allCartProducts.size());
        order.setDate(new Date());
        order.setStatus("Realizado");

        orderService.save(order);
        //  Add order to a user
        if (user.getUserOrders() == null) {
            ArrayList<Order> arrayList = new ArrayList<>();
            arrayList.add(order);
            user.setUserOrders(arrayList);
        } else {
            user.getUserOrders().add(order);
        }
        this.save(user);
    }

    public void deleteOrder(User user) {
        user.setUserOrders(null);
        this.save(user);
    }
}
