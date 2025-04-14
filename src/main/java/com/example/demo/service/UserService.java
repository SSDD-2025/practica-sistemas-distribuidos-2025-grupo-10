package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.model.Category;
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
    @Autowired
    private ProductService productService;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

//    public Collection<User> findAll() {
//        return userRepository.findAll();
//    }
    public Collection<UserDTO> findAll() {
        return toDTOs(userRepository.findAll());
    }

    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByID(Long id) {
        return userRepository.findById(id);
    }

    public UserDTO findByUsername(String username) {
        return toDTO(userRepository.findByUsername(username).get());
    }
    public User findByUsernameEntity(String username) {
        return (userRepository.findByUsername(username).get());
    }

    public UserDTO getUser(String name){
        return toDTO(userRepository.findByUsername(name).orElseThrow());
    }

    public UserDTO deleteUserById(Long id) {
        User userDeleted = userRepository.findById(id).orElseThrow();
        UserDTO dto = toDTO(userDeleted);
        userRepository.deleteById(id);
        return dto;
    }



    public void addProductToCart2(Long productId, String userDtoName) {
        UserDTO byUsername = findByUsername(userDtoName);

        List<ProductDTO> userProducts = byUsername.userProducts();
        ArrayList<ProductDTO> products;
        if (userProducts != null) {
            products = new ArrayList<>(userProducts);
            ProductDTO newProduct = productService.findProductById(productId);
            products.add(newProduct);
        }else{
            products = new ArrayList<>();
            ProductDTO newProduct = productService.findProductById(productId);
            products.add(newProduct);
        }


        UserDTO updatedUser = new UserDTO(
                byUsername.id(),
                byUsername.username(),
                byUsername.email(),
                byUsername.roles(),
                products,
                byUsername.userOrders()
        );

        userRepository.save(toDomain(updatedUser));
    }

    /*
    public void productsFromCartIntoOrder(User user) {
        List<Product> allCartProducts = new ArrayList<>(user.getUserProducts());
        user.getUserProducts().clear();
        userRepository.save(user);
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
        userRepository.save(user);
    }

     */
    public void productsFromCartIntoOrder2(String username) {
            UserDTO byUsername = findByUsername(username);
            List<ProductDTO> allCartProducts = new ArrayList<>(byUsername.userProducts());
            UserDTO updatedUser = new UserDTO(
                    byUsername.id(),
                    byUsername.username(),
                    byUsername.email(),
                    byUsername.roles(),
                    new ArrayList<>(),
                    byUsername.userOrders()
            );
            userRepository.save(toDomain(updatedUser));
            //  Add all products from cart into an order
            BigDecimal total = allCartProducts.stream()
                    .map(ProductDTO::price)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            OrderDTO order = new OrderDTO(null, total, allCartProducts.size(), new Date(), "Realizado", allCartProducts);
            //orderService.save(order);


        Order domain = orderService.toDomain(order);
        orderService.saveEntity(domain);

        User byUsername2 = findByUsernameEntity(username);

        if (byUsername2.getUserOrders() == null) {
                ArrayList<Order> arrayList = new ArrayList<>();
                arrayList.add(domain);
                byUsername2.setUserOrders(arrayList);
        } else {
            byUsername2.getUserOrders().add(domain);
        }
        userRepository.save(byUsername2);
        orderService.saveEntity(domain);
            /*

            UserDTO byUsernam2 = findByUsername(username);
            //  Add order to a user
            if (byUsernam2.userOrders() == null) {
                ArrayList<OrderDTO> arrayList = new ArrayList<>();
                arrayList.add(order);

                UserDTO updatedUser2 = new UserDTO(
                        byUsernam2.id(),
                        byUsernam2.username(),
                        byUsernam2.email(),
                        byUsernam2.roles(),
                        byUsernam2.userProducts(),
                        arrayList
                );
                userRepository.save(toDomain(updatedUser2));
            } else {
                List<OrderDTO> orderDTOS = byUsernam2.userOrders();
                ArrayList<OrderDTO> actualizado = new ArrayList<>(orderDTOS);
                actualizado.add(order);
                UserDTO updatedUser2 = new UserDTO(
                        byUsernam2.id(),
                        byUsernam2.username(),
                        byUsernam2.email(),
                        byUsernam2.roles(),
                        byUsernam2.userProducts(),
                        actualizado
                );
                userRepository.save(toDomain(updatedUser2));
            }
            orderService.save(order);
             */
    }

    public void deleteOrder(User user) {
        user.setUserOrders(null);
        //  User.setorder(user.getorders.remove(elorder))
        userRepository.save(user);
    }

    private UserDTO toDTO(User user){
        return mapper.toDTO(user);
    }

    private User toDomain(UserDTO userDTO) {
        return mapper.toDomain(userDTO);
    }

    private Collection<UserDTO> toDTOs(Collection<User> user) {
        return mapper.toDTOs(user);
    }

    public void removeProductFromAllUsers(ProductDTO product) {
        for (UserDTO u : findAll()) {
            if (u.userProducts().contains(product)) {
                u.userProducts().remove(product);
                save(u);
            }
        }
    }
    public void save(UserDTO u) {
        userRepository.save(toDomain(u));
    }

    public boolean existsByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public UserDTO createNewUser(NewUserRequestDTO newUser){
        User user = new User();
        user.setUsername(newUser.username());
        user.setRoles(newUser.roles());
        user.setPassword(newUser.password());
        user.setEmail(newUser.email());
        userRepository.save(user);
        return toDTO(user);
    }

}
