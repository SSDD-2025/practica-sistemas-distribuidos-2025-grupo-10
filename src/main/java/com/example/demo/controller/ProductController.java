package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;
    @GetMapping("/products")
    public String StringShowProducts(Model model){
        model.addAttribute("products", productService.findall());
        return "products";
    }
    @GetMapping("products/add")
    public String showFormAdd(Model model){
        model.addAttribute("product", new Product());
        return "addProduct";
    }
    @PostMapping("products/add")
    public String addProduct(Product product){
        productService.save(product);
        return "redirect:/products";
    }
    @GetMapping("/products/{id}")
    public String showProduct(Model model, @PathVariable long id){
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "showProduct";
    }
    @PostMapping("/product/{id}/delete")
    public String deleteProduct(Model model, @PathVariable long id){
        productService.deleteProductById(id);
        return "redirect:/products";
    }
}
