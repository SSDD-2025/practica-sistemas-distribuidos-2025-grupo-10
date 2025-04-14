package com.example.demo.controller.web;

import com.example.demo.dto.UserDTO;
import com.example.demo.model.Order;
import com.example.demo.model.User;
import com.example.demo.service.OrderService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Date;
import java.util.Optional;

@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;


    @GetMapping("/orders")
    public String showOrders(Model model, Principal principal) {
        String username = principal.getName();
        UserDTO user = userService.findByUsername(username);
        model.addAttribute("orders", user.userOrders());
        return "orders";
    }


    @GetMapping("/orders/add")
    public String showFormAdd(Model model) {
        model.addAttribute("order", new Order(BigDecimal.ZERO, 0, new Date(), null, "")); // Crear un pedido con valores predeterminados
        return "addOrder";
    }

    @PostMapping("/orders/add")
    public String addOrder(Order order) {
        orderService.save(order); // We save the order
        return "redirect:/orders"; // It redirects to order list
    }

    @GetMapping("/orders/{id}")
    public String showOrder(Model model, @PathVariable Long id) {
        Optional<Order> order = orderService.findById(id);
        if (order.isPresent()) {
            model.addAttribute("order", order.get());
            return "order";
        } else {
            return "orders";
        }
    }

    @GetMapping("/orders/manage")
    public String showManageOrders(Model model, Principal principal) {
        String name = principal.getName();
        UserDTO byUsername = userService.findByUsername(name);
        model.addAttribute("orders", byUsername.userOrders());
        return "deleteOrders";
    }

    @PostMapping("/orders/{id}/delete")
    public String deleteOrder(@PathVariable Long id) {
        orderService.deleteOrderById(id);
        return "redirect:/orders/manage";  // Refresh the page with updated list
    }

}
