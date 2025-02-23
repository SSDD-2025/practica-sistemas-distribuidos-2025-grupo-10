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
public class OrderController{

    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;


    // Mostrar todos los pedidos
    @GetMapping("/orders")
    public String showOrders(Model model) {
        model.addAttribute("orders", orderService.findAll());
        return "orders"; // vista que muestra la lista de pedidos
    }

    // Mostrar formulario para añadir un nuevo pedido
    @GetMapping("/orders/add")
    public String showFormAdd(Model model) {
        model.addAttribute("order", new Order(BigDecimal.ZERO, 0, new Date(),  null, "")); // Crear un pedido con valores predeterminados
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
        Optional<Order> order = orderService.findById(id);
        if (order.isPresent()){
            model.addAttribute("order", order.get());
            return "order";
        } else{
            return "orders";
        }
    }

    // Eliminar un pedido
    @PostMapping("/orders/{id}/delete")
    public String deleteOrder(@PathVariable Long id) {
        Optional<Order> order = orderService.findById(id);
        if (order.isPresent()){
            orderService.deleteOrderById(id);
            return "order";
        } else{
            return "orders";
        }
    }

}
