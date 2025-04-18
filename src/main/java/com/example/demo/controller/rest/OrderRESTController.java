package com.example.demo.controller.rest;

import com.example.demo.dto.OrderDTO;
import com.example.demo.dto.OrderMapper;
import com.example.demo.model.Order;
import com.example.demo.repository.OrderRepository;
import java.util.NoSuchElementException;

import com.example.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

@RestController
public class OrderRESTController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/api/orders")
    public Collection<OrderDTO> getOrders(){
        return orderService.findAll();
    }

    @GetMapping("/api/orders/{id}")
    public OrderDTO getOrder(@PathVariable Long id) {
        return orderService.getById(id);
    }

    @DeleteMapping("/api/orders/{id}")
    public OrderDTO deleteOrder(@PathVariable long id){
        return orderService.deleteOrderById(id);
    }

}
