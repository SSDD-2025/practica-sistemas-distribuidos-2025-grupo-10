package com.example.demo.service;

import com.example.demo.dto.UserDTO;
import com.example.demo.dto.UserMapper;
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

    private final UserRepository userRepository;
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserMapper mapper;

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

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con username: " + username));
    }
    public UserDTO getUser(String name){
        return mapper.toDTO(userRepository.findByUsername(name).orElseThrow());
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
        //  User.setorder(user.getorders.remove(elorder))
        this.save(user);
    }
}
