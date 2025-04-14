package com.example.demo.dto;

import com.example.demo.model.Product;
import com.example.demo.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collection;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDTO(User user);


    User toDomain(UserDTO user);


    Collection<UserDTO> toDTOs(Collection<User> user);
}
