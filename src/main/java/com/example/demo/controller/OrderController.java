package com.example.demo.controller;

import com.example.demo.model.Category;
import com.example.demo.model.Order;
import com.example.demo.model.User;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.OrderService;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class OrderController implements CommandLineRunner {

    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    // Mostrar todos los pedidos
    @GetMapping("/orders")
    public String showOrders(Model model) {
        model.addAttribute("orders", orderService.findAll());
        return "orders"; // vista que muestra la lista de pedidos
    }

    // Mostrar formulario para añadir un nuevo pedido
    @GetMapping("/orders/add")
    public String showFormAdd(Model model) {
        model.addAttribute("order", new Order(BigDecimal.ZERO, 0, new Date(), null, null, "")); // Crear un pedido con valores predeterminados
        return "addOrder"; // vista para agregar un nuevo pedido
    }

    // Agregar un nuevo pedido
    @PostMapping("/orders/add")
    public String addOrder(Order order) {
        orderService.save(order); // Guardamos el pedido
        return "redirect:/orders"; // Redirigimos a la lista de pedidos
    }

    // Ver detalles de un pedido
    @GetMapping("/orders/{id}")
    public String showOrder(Model model, @PathVariable Long id) {
        Order order = orderService.getOrderById(id); // Cambiar id a Long
        model.addAttribute("order", order);
        return "showOrder"; // Vista que muestra los detalles del pedido
    }

    // Eliminar un pedido
    @PostMapping("/orders/{id}/delete")
    public String deleteOrder(@PathVariable Long id) {
        orderService.deleteOrderById(id); // Eliminamos el pedido
        return "redirect:/orders"; // Redirigimos a la lista de pedidos
    }

    @Override
    public void run(String... args) throws Exception {
        // Guardar categorías en la base de datos
        //categoryRepository.save(new Order(new BigDecimal(5), 5, new Date(), new User(), new ArrayList<>(), "Hola"));
        categoryRepository.save(new Category("Shrek"));
        categoryRepository.save(new Category("Bocadillo de panceta"));

        // Obtener y mostrar todas las categorías
        List<Category> categories = categoryRepository.findAll();
        System.out.println("Categories found with findAll():");
        System.out.println("--------------------------------");
        for (Category category : categories) {
            System.out.println(category);
        }
        System.out.println();

        // Buscar una categoría por ID
        Optional<Category> foundCategory = categoryRepository.findById(2L);
        if (foundCategory.isPresent()) {
            System.out.println("Category found with findById(2L):");
            System.out.println("--------------------------------");
            System.out.println(foundCategory.get());
            System.out.println();
        }

        // Verificar si existe una categoría con ID 4
        boolean exists = categoryRepository.existsById(4L);
        System.out.println("Does category with ID 4 exist? " + exists);
        System.out.println();

        // Buscar categorías por nombre
        List<Category> shrekCategories = categoryRepository.findByName("Shrek");
        System.out.println("Categories found with findByName('Shrek'):");
        System.out.println("------------------------------------------");
        for (Category category : shrekCategories) {
            System.out.println(category);
        }
        System.out.println();

        System.out.println("Eliminar si existe el id2");
        System.out.println("------------------------------------------");
        // Eliminar categoría con ID 2 si existe
        foundCategory.ifPresent(category -> categoryRepository.deleteById(2L));
        System.out.println();

        System.out.println("Eliminar en base a una clave no primaria -> name");
        System.out.println("------------------------------------------");
        List<Category> categoriesTwo = categoryRepository.findByName("Hola");
        categoryRepository.deleteAll(categoriesTwo);
        System.out.println();


        // Mostrar todas las categorías después de las modificaciones
        categories = categoryRepository.findAll();
        System.out.println("Categories after modifications:");
        System.out.println("(Deberia quedar solo 1 -> bocadillo de panceta)");
        System.out.println("-------------------------------");
        for (Category category : categories) {
            System.out.println(category);
        }
        System.out.println();

        System.out.println("Operaciones de modificacion");
        System.out.println("-------------------------------");
        List<Category> categoriesThree = categoryRepository.findByName("Bocadillo de panceta");
        Category first = categoriesThree.get(0);
        first.setName("Bocadillo cambiado");
        categoryRepository.save(first);
        System.out.println("Cambio: " + first.toString());
        System.out.println();

        System.out.println("Consulta por nombre cambiado");
        System.out.println("-------------------------------");
        List<Category> categoriesFour = categoryRepository.findByName("Bocadillo cambiado");
        for (Category category : categoriesFour) {
            System.out.println(category);
        }
    }
}
