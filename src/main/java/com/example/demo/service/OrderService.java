package com.example.demo.service;

import com.example.demo.dto.OrderDTO;
import com.example.demo.dto.OrderMapper;
import com.example.demo.dto.ProductMapper;
import com.example.demo.dto.UserDTO;
import com.example.demo.model.Order;
import com.example.demo.model.User;
import com.example.demo.repository.OrderRepository;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;


@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderMapper mapper;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Optional<Order> findById(long id) {
        return orderRepository.findById(id);
    }

    public OrderDTO getById(long id) {
        return toDTO(orderRepository.findById(id).orElseThrow());
    }

    public boolean exist(long id) {
        return orderRepository.existsById(id);
    }

    public Collection<OrderDTO> findAll() {
        return toDTOs(orderRepository.findAll());
    }

    public void save(OrderDTO order) {
        orderRepository.save(toDomain(order));
    }


    public void saveEntity(Order order) {
        orderRepository.save((order));
    }

    public OrderDTO deleteOrderById(Long id) {
        Order orderDeleted = orderRepository.findById(id).orElseThrow();
        OrderDTO dto = toDTO(orderDeleted);
        orderRepository.deleteById(id);
        return dto;
    }

    private OrderDTO toDTO(Order order){
        return mapper.toDTO(order);
    }
    public Order toDomain(OrderDTO orderDTO){
        return mapper.toDomain(orderDTO);
    }
    private Collection<OrderDTO> toDTOs(Collection<Order> orders) {
        return mapper.toDTOs(orders);
    }

}


