package com.example.demo.controller;

import com.example.demo.model.Category;
import com.example.demo.model.Product;
import com.example.demo.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@Controller
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @ModelAttribute("categories")
    public List<Category> getCategories() {
        return categoryService.findAll();
    }

    @GetMapping("/")
    public String showHomePage() {
        return "mainPage";
    }

    @GetMapping("/categories")
    public String showCategories(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        return "addCategory";
    }

    @GetMapping("/categories/add")
    public String showFormAdd(Model model) {
        model.addAttribute("category", new Category(""));
        return "addCategory";
    }

    @GetMapping("/categories/{id}")
    public String showCategory(Model model, @PathVariable Long id) {
        Optional<Category> category = categoryService.findCategoryById(id);
        model.addAttribute("category", category);
        return "showCategory";
    }

    @GetMapping("/category/{id}")
    public String showProductsByCategory(@PathVariable("id") Long categoryId, Model model) {
        Optional<Category> categoryOpt = categoryService.findCategoryById(categoryId);

        if (categoryOpt.isPresent()) {
            Category category = categoryOpt.get();
            List<Product> products = category.getProducts(); //It obtains all products from a specific category

            model.addAttribute("category", category);
            model.addAttribute("products", products);

            return "productsByCategories";
        } else {
            return "redirect:/mainPage"; // Redirect if the category does not exist
        }
    }

    @PostMapping("/categories/{id}/delete")
    public String deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategoryById(id);
        return "redirect:/categories/manage";
    }

    @GetMapping("/categories/manage")
    public String showManageCategories(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        return "deleteCategories";
    }

    @PostMapping("/categories/add")
    public String addCategory(Category category, Model model) {
        boolean added = categoryService.addCategory(category);
        if (!added) {
            model.addAttribute("error", "La categoría ya existe");
            return "addCategory";
        }
        return "redirect:/categories/add";
    }

}
