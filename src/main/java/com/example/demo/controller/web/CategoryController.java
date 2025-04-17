package com.example.demo.controller.web;

import com.example.demo.dto.CategoryDTO;
import com.example.demo.dto.ProductDTO;
import com.example.demo.service.CategoryService;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;



    @GetMapping("/")
    public String showHomePage(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        return "mainPage";
    }

    @GetMapping("/categories")
    public String showCategories(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        return "addCategory";
    }

    @GetMapping("/categories/add")
    public String showFormAdd(Model model) {
        model.addAttribute("category", new CategoryDTO(0L, ""));
        model.addAttribute("categories", categoryService.findAll());
        return "addCategory";
    }

    @GetMapping("/categories/{id}")
    public String showCategory(Model model, @PathVariable Long id) {
        model.addAttribute("categories", categoryService.findAll());
        CategoryDTO category = categoryService.findCategoryById(id);
        model.addAttribute("category", category);
        return "showCategory";
    }

    // Método en Product service no da error, falta arreglar este
    @GetMapping("/category/{id}")
    public String showProductsByCategory(@PathVariable("id") Long categoryId, Model model) {
        model.addAttribute("categories", categoryService.findAll());
        try {
            CategoryDTO categoryDTO = categoryService.findCategoryById(categoryId);
            List<ProductDTO> productsByCategoryDTOs = productService.getProductsByCategory(categoryDTO.id());

            model.addAttribute("category", categoryDTO);
            model.addAttribute("products", productsByCategoryDTOs);

            return "productsByCategories";
        } catch (NoSuchElementException e) {
            return "redirect:/mainPage";
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
    public String addCategory(@ModelAttribute("category") CategoryDTO categoryDTO, Model model) {
        /*boolean added = categoryService.addCategory(category);
        if (!added) {
            model.addAttribute("error", "La categoría ya existe");
            return "addCategory";
        }
        return "redirect:/categories/add";*/

        try {
            categoryService.addCategory(categoryDTO);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "La categoría ya existe");
            model.addAttribute("categories", categoryService.findAll());
            return "addCategory";
        }
        return "redirect:/categories/add";
    }
    /*
    @PostMapping("/categories/add")
    public String addCategory(@ModelAttribute("category") CategoryDTO categoryDTO, Model model) {
        if (categoryService.findAll().contains(categoryDTO)){
            categoryService.addCategory(categoryDTO);
        }else {
            model.addAttribute("error", "La categoría ya existe");
            return "addCategory";
        }
        return "redirect:/categories/add";
        /* No funciona
        try {
            categoryService.addCategory(categoryDTO);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "La categoría ya existe");
            return "addCategory";
        }
        return "redirect:/categories/add";
    }
    */


}


