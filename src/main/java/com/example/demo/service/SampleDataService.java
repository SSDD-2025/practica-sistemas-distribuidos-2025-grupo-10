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
        productService.save(product1);

        //  Agregar producto al carrito del usuario
        User user = new User("Alberto", "password", "Hola");
        user.getUserProducts().add(product1);
        userService.save(user);

        //  Creamos order
        Order order = new Order();
        //  Eliminamos el producto del carrito
        user.getUserProducts().remove(product1);
        userService.save(user);
        //  Añadimos el produto al order
        order.getProducts().add(product1);
        orderService.save(order);

        //  Añadimos el order al usuario
        user.getUserOrders().add(order);
        userService.save(user);

    }

    //  Todo save, o flujo de guardado de datos funciona
    //  Si eliminamos una cateogoria, los productos.idCategory == null
    //  Falta por probar si eliminamos un un pedido, no sabemos que pasa con los productos del pedido
    //  Si eliminamos un usuario, no sabvemos que pasa con el pedido
    //  Si eliminamos un usuario, no sabemos que pasa con su cesta (la mas facil)

    /*
    @PostConstruct
    public void init() {
        /*  FUNCIONAL
        //  Dado un producto con su categoria, se elimina su categoria y debe de estar a null
        categoryService.save(new Category("Categoria de cositas XD"));
        categoryService.save(new Category("cate2"));
        //  Inicializar un producto1 a categoria 1
        List<Category> all = categoryService.findAll();
        Category category = all.get(0);
        Category category2 = all.get(1);

        Product product1 = new Product("producto numero 1", new BigDecimal(15));
        Product product2 = new Product("producto numero 2", new BigDecimal(15));
        product1.setCategory(category);
        product2.setCategory(category2);
        productService.save(product1);
        productService.save(product2);

        categoryService.deleteCategoryById(category.getId());
        FIN FUNCIONAL
        */


        /*
        User user = new User("Alberto", "password", "Hola");
        //  Creamos order
        Order order = new Order();
        //  Eliminamos el producto del carritp
        userService.save(user);
        //  Añadimos el produto al order
        order.getProducts().add(product1);
        orderService.save(order);

        user.getUserOrders().add(order);
        userService.save(user);


        orderService.deleteOrderById(order.getId());

         */
    //}
    //*/
}
