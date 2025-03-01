package com.example.demo.service;

import com.example.demo.model.Category;
import com.example.demo.model.Order;
import com.example.demo.model.Product;
import com.example.demo.model.User;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.service.ImageUtils;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

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


    /*
    @PostConstruct
    public void init() {


        //  Inicializar categoria
        Category category = new Category("Categoria de cositas XD");
        categoryService.save(category);


        //  Inicializar un producto1 a categoria 1
        Product productToSave = new Product("producto numero 1", new BigDecimal(15));
        productService.saveProductCategory(productToSave, category);


        //  Agregar producto al carrito del usuario
        User user = new User("Alberto", "password", "Hola");
        userService.addProductToCart(productToSave, user);

        //  Eliminamos todos los productos del carrito y añadimos el order al usuario
        userService.productsFromCartIntoOrder(user);

    }


     */


    //  Todo save, o flujo de guardado de datos funciona
    //  Si eliminamos una cateogoria, los productos.idCategory == null
    //  Falta por probar si eliminamos un un pedido, no sabemos que pasa con los productos del pedido. Si eliminamos un pedido. Se queda en la bbdd
    //  Si eliminamos un usuario, no sabvemos que pasa con el pedido
    //  Si eliminamos un usuario, no sabemos que pasa con su cesta (la mas facil)


    @PostConstruct
    public void init() throws IOException {
        //  FUNCIONAL
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
        productService.save(product1, null);
        productService.save(product2, null);

        categoryService.deleteCategoryById(category.getId());

        //  Aqui empieza
        User user = new User("Alberto", "password", "<PASSWORD>");
        userService.addProductToCart(product1, user);
        userService.addProductToCart(product2, user);
        userService.productsFromCartIntoOrder(user);


        Product product3 = new Product();
        productService.save(product3, null);
        userService.addProductToCart(product3, user);


        categoryService.deleteCategoryById(category2.getId());
        categoryService.deleteCategoryById(category.getId());
    }
    private void saveproductWithURLImage(Product product, String image) throws IOException {
        product.setImageFile(imageUtils.localImageToBlob("images/" + image));
        productService.save(product, null);
    }

/*

        Collection<Order> all1 = orderService.findAll();
        ArrayList<Order> orders = new ArrayList<>(all1);
        for (Order order : orders) {
            orderService.deleteOrderById(order.getId());
        }



 */



        /*
        user.getUserProducts().remove(product1);



        productService.deleteProductById(product1.getId());



         */





        //  userService.deleteOrder(user);
        //userService.deleteUserById(user.getId());
        //  Si eliminas un usuario, eliminas sus pedidos, pero no eliminas los productos en si

        //  Dado el ultimo cambio, si eliminas un usuario, no se elimina el pedido (pero no hay relacion entre pedido)


        //  FIN FUNCIONAL



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

        //  ELiminar el pedido x


}
