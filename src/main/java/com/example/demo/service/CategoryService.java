package com.example.demo.service;

import com.example.demo.model.Category;
import com.example.demo.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
        initializeData();
    }

    public void save(Category category) {
        categoryRepository.save(category);
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Optional<Category> findCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    public void deleteCategoryById(Long id) {
        categoryRepository.deleteById(id);
    }

    public void initializeData(){
        categoryRepository.save(new Category("Hola"));  // 1
        categoryRepository.save(new Category("Shrek")); //  2
        categoryRepository.save(new Category("Bocadillo de panceta"));  //3

        // Obtener y mostrar todas las categorías
        List<Category> categories = categoryRepository.findAll();
        System.out.println("Categories found with findAll():");
        System.out.println("--------------------------------");
        for (Category category : categories) {
            System.out.println(category);
        }
        System.out.println();

        // Buscar una categoría por ID
        Optional<Category> foundCategory = categoryRepository.findById(2L);
        if (foundCategory.isPresent()) {
            System.out.println("Category found with findById(2L):");
            System.out.println("--------------------------------");
            System.out.println(foundCategory.get());
            System.out.println();
        }

        // Verificar si existe una categoría con ID 4
        boolean exists = categoryRepository.existsById(4L);
        System.out.println("Does category with ID 4 exist? " + exists);
        System.out.println();

        // Buscar categorías por nombre
        List<Category> shrekCategories = categoryRepository.findByName("Shrek");
        System.out.println("Categories found with findByName('Shrek'):");
        System.out.println("------------------------------------------");
        for (Category category : shrekCategories) {
            System.out.println(category);
        }
        System.out.println();

        System.out.println("Eliminar si existe el id2");
        System.out.println("------------------------------------------");
        // Eliminar categoría con ID 2 si existe
        foundCategory.ifPresent(category -> categoryRepository.deleteById(2L));
        System.out.println();

        System.out.println("Eliminar en base a una clave no primaria -> name");
        System.out.println("------------------------------------------");
        List<Category> categoriesTwo = categoryRepository.findByName("Hola");
        categoryRepository.deleteAll(categoriesTwo);
        System.out.println();


        // Mostrar todas las categorías después de las modificaciones
        categories = categoryRepository.findAll();
        System.out.println("Categories after modifications:");
        System.out.println("(Deberia quedar solo 1 -> bocadillo de panceta)");
        System.out.println("-------------------------------");
        for (Category category : categories) {
            System.out.println(category);
        }
        System.out.println();

        System.out.println("Operaciones de modificacion");
        System.out.println("-------------------------------");
        List<Category> categoriesThree = categoryRepository.findByName("Bocadillo de panceta");
        Category first = categoriesThree.get(0);
        first.setName("Bocadillo cambiado");
        categoryRepository.save(first);
        System.out.println("Cambio: " + first.toString());
        System.out.println();

        System.out.println("Consulta por nombre cambiado");
        System.out.println("-------------------------------");
        List<Category> categoriesFour = categoryRepository.findByName("Bocadillo cambiado");
        for (Category category : categoriesFour) {
            System.out.println(category);
        }
    }
}
