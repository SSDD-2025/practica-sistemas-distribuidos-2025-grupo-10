package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    /*
    @PersistenceContext
    private EntityManager entityManager;

    // Encontrar todos los usuarios
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return entityManager.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    // Guardar un usuario
    @Transactional
    public void save(User user) {
        if (user.getId() == null) {
            entityManager.persist(user);
        } else {
            entityManager.merge(user);
        }
    }

    // Obtener un usuario por ID
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return entityManager.find(User.class, id);
    }

    // Eliminar un usuario por ID
    @Transactional
    public void deleteUserById(Long id) {
        User user = entityManager.find(User.class, id);
        if (user != null) {
            entityManager.remove(user);
        }
    }

     */

    private final UserRepository userRepository;

    public UserService (UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void save (User user){
        userRepository.save(user);
    }

    public Collection<User> findAll(){
        return userRepository.findAll();
    }

    public Optional<User> findUserById (Long id){
        return userRepository.findById(id);
    }

    public void deleteUserById(Long id){
        userRepository.deleteById(id);
    }













}
