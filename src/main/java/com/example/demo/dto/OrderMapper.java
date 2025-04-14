package com.example.demo.dto;

import com.example.demo.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderDTO toDTO(Order order);
    List<OrderDTO> toDTOs(Collection<Order> orders);
    Order toDomain(OrderDTO orderDTO);

}
