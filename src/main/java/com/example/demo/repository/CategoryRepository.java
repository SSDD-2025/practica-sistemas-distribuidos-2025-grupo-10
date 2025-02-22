package com.example.demo.repository;

import com.example.demo.model.Category;
import com.example.demo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByName (String name);    //  La consulta se basa en el nombre de "findByName"
    //  Se entiende que findBy es como una palabra reservada y name(o NAME o NaMe) debe de ser un
    //  atributo dentro de la clase
    //  El nombre del parametro da igual
}
