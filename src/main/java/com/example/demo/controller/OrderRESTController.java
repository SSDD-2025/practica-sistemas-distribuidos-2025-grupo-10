package com.example.demo.controller;

import com.example.demo.dto.OrderDTO;
import com.example.demo.dto.OrderMapper;
import com.example.demo.model.Order;
import com.example.demo.repository.OrderRepository;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

@RestController
//@RequestMapping("/api/orders") como todas las URLS empiezan igual se pone eso y luego a cada método se le pondría su parte propia, ejemplo {id}
//Cambiar cuando pueda probar esta parte
public class OrderRESTController {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderMapper mapper;

    @GetMapping("/api/orders")
    public Collection<OrderDTO> getOrders(){
        return toDTOs(orderRepository.findAll());
    }

    @GetMapping("/api/orders/{id}")
    public OrderDTO getOrder(@PathVariable Long id) {
        return toDTO(orderRepository.findById(id).orElseThrow());
    }

    // Faltaría un crear order, hacer cuando esté el rest de products
    // Los pedidos no se pueden editar

    @DeleteMapping("/api/orders/{id}")
    public OrderDTO deleteOrder(@PathVariable long id){
        Order order = orderRepository.findById(id).orElseThrow();
        orderRepository.deleteById(id);
        return toDTO(order);
    }
    private OrderDTO toDTO(Order order){
        return mapper.toDTO(order);
    }

    private Order toDomain(OrderDTO orderDTO){
        return mapper.toDomain(orderDTO);
    }

    private Collection<OrderDTO> toDTOs(Collection<Order> orders){
        return mapper.toDTOs(orders);
    }

}
