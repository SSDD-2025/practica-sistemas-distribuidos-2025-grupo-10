package com.example.demo.controller;

import com.example.demo.model.Category;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.CategoryService;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@Controller
public class CategoryController{

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/categories")
    public String showCategories(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        return "categories";  // Asegúrate de que "categories.html" esté bien configurado
    }

    @GetMapping("/categories/add")
    public String showFormAdd(Model model) {
        model.addAttribute("category", new Category(""));  // Crear una categoría vacía con un nombre temporal
        return "addCategory";  // Asegúrate de que "addCategory.html" esté bien configurado
    }

    @PostMapping("/categories/add")
    public String addCategory(Category category) {
        categoryService.save(category);
        return "redirect:/categories";  // Redirige después de agregar la categoría
    }

    @GetMapping("/categories/{id}")
    public String showCategory(Model model, @PathVariable Long id) {
        Optional<Category> category = categoryService.findCategoryById(id);
        model.addAttribute("category", category);
        return "showCategory";  // Asegúrate de que "showCategory.html" esté bien configurado
    }

    @PostMapping("/categories/{id}/delete")
    public String deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategoryById(id);
        return "redirect:/categories";  // Redirige después de eliminar la categoría
    }

    @GetMapping("/")
    public String showHomePage(Model model) {
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories); // Pasamos las categorías al HTML
        return "mainPage";
    }
}
