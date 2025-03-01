package com.example.demo.service;

import com.example.demo.model.Order;
import com.example.demo.model.Product;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private OrderService orderService;
    /*
    @PersistenceContext
    private EntityManager entityManager;

    // Encontrar todos los usuarios
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return entityManager.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    // Guardar un usuario
    @Transactional
    public void save(User user) {
        if (user.getId() == null) {
            entityManager.persist(user);
        } else {
            entityManager.merge(user);
        }
    }

    // Obtener un usuario por ID
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return entityManager.find(User.class, id);
    }

    // Eliminar un usuario por ID
    @Transactional
    public void deleteUserById(Long id) {
        User user = entityManager.find(User.class, id);
        if (user != null) {
            entityManager.remove(user);
        }
    }

     */

    private final UserRepository userRepository;

    public UserService (UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void save (User user){
        userRepository.save(user);
    }

    public Collection<User> findAll(){
        return userRepository.findAll();
    }

    public Optional<User> findUserById (Long id){
        return userRepository.findById(id);
    }

    public void deleteUserById(Long id){
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
        Order order = new Order();
        List<Product> allCartProducts = new ArrayList<>(user.getUserProducts());
        user.getUserProducts().clear();
        this.save(user);
        //  Añadimos el produto al order

        order.getProducts().addAll(allCartProducts);
        orderService.save(order);
        //  Añadimos el order al usuario
        if (user.getUserOrders() == null){
            ArrayList<Order> arrayList = new ArrayList<>();
            arrayList.add(order);
            user.setUserOrders(arrayList);
        }else{
            user.getUserOrders().add(order);
        }
        this.save(user);
    }
    public void deleteOrder(User user) {
        user.setUserOrders(null);
        this.save(user);
    }
}
