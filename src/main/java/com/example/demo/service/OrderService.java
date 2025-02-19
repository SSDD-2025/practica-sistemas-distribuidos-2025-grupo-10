package com.example.demo.service;

import com.example.demo.model.Order;
import com.example.demo.model.User;
import com.example.demo.model.Product;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class OrderService {

    private ConcurrentMap<Long, Order> orders = new ConcurrentHashMap<>();
    private AtomicLong nextId = new AtomicLong();

    public OrderService() {
        // Creamos una lista de productos de ejemplo
        List<Product> products = new ArrayList<>();
        products.add(new Product("Laptop", 1000));
        products.add(new Product("Phone", 800));

        // Creamos el pedido de ejemplo
        save(new Order(nextId.getAndIncrement(), new BigDecimal("1800.00"), 2, new Date(), new User(), products, "pending"));
    }

    public void save(Order order) {
        long id = nextId.getAndIncrement();
        order.setId((int) id);  // Asegúrate de que el id es un tipo int si lo necesitas
        this.orders.put(id, order);
    }

    public Collection<Order> findAll() {
        return orders.values();
    }

    public Order getOrderById(long id) {
        return orders.get(id);
    }

    public void deleteOrderById(long id) {
        this.orders.remove(id);
    }
}


