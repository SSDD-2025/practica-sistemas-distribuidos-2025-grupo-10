package com.example.demo.controller;

import com.example.demo.model.Category;
import com.example.demo.model.Product;
import com.example.demo.model.User;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.service.CategoryService;
import com.example.demo.service.ProductService;
import com.example.demo.service.UserService;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserService userService;

    @GetMapping("/products")
    public String showProducts(Model model) {
        Collection<Product> products = productService.findall();

        if (products == null || products.isEmpty()) {
            System.out.println("No hay productos disponibles.");
        } else {
            System.out.println("---- LISTA DE PRODUCTOS ----");
            for (Product p : products) {
                System.out.println("Producto: " + p.getName());
            }
        }

        model.addAttribute("products", products);
        return "products";
    }

    @GetMapping("/manageProducts")
    public String showManageProductsPage(Model model) {
        model.addAttribute("products", productService.findall());
        return "manageProducts";
    }

    @GetMapping("products/add")
    public String showFormAdd(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.findAll());
        return "addProduct";
    }

    @PostMapping("/products/add")
    public String addProduct(@ModelAttribute Product product, MultipartFile imageField,
                             @RequestParam("categoryId") Long categoryId) throws IOException {
        Category category = categoryService.findCategoryById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada con ID: " + categoryId));

        product.setCategory(category);
        productService.save(product, imageField);
        return "redirect:/products/add?success=true";
    }

    @PostMapping("/cart/add/{id}")
    public String addProductTocart(@PathVariable long id) throws IOException {
        User user = userService.findUserById(1L)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + 1));
        Product product = productService.findProductById(id).orElseThrow();
        userService.addProductToCart(product, user);
        return "redirect:/cart";
    }

    @GetMapping("products/{id}/image")
    public ResponseEntity<Object> downloadImage(@PathVariable long id) throws SQLException {

        Optional<Product> op = productService.findProductById(id);

        if (op.isPresent() && op.get().getImageFile() != null) {

            Blob image = op.get().getImageFile();
            Resource file = new InputStreamResource(image.getBinaryStream());

            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                    .contentLength(image.length()).body(file);

        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/products/{id}")
    public String showProduct(Model model, @PathVariable long id) {
        Optional<Product> product = productService.findProductById(id);
        model.addAttribute("product", product);
        return "showProduct";
    }

    @PostMapping("/products/{id}/delete")
    public String deleteProduct(@PathVariable long id) {
        productService.deleteProduct(id);
        return "redirect:/products/manage"; // Refresh the page with updated list
    }

    @PostMapping("/products/{id}/update")
    public String updateProduct(@PathVariable long id,
                                @RequestParam String name,
                                @RequestParam BigDecimal price,
                                @RequestParam(required = false) Long categoryId,
                                @RequestParam(required = false) MultipartFile imageField) throws IOException {

        Product product = productService.findProductById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

        product.setName(name);
        product.setPrice(price);

        if (categoryId != null) {
            Category category = categoryService.findCategoryById(categoryId)
                    .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada"));
            product.setCategory(category);
        } else {
            product.setCategory(null);
        }

        // It only updates the image if the field is not empty
        if (imageField != null && !imageField.isEmpty()) {
            product.setImageFile(BlobProxy.generateProxy(imageField.getInputStream(), imageField.getSize()));
        }

        productService.save(product);
        return "redirect:/products";
    }

    @GetMapping("/products/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Product> product = productService.findProductById(id);
        if (product.isPresent()) {
            Product existingProduct = product.get();

            // If the category is null we dont access to its ID
            Long categoryId = (existingProduct.getCategory() != null) ? existingProduct.getCategory().getId() : null;

            model.addAttribute("product", existingProduct);
            model.addAttribute("categoryId", categoryId);
            model.addAttribute("categories", categoryService.findAll());

            return "editProduct";
        }
        return "redirect:/products";
    }

    @GetMapping("/products/manage")
    public String showManageProducts(Model model) {
        model.addAttribute("products", productService.findall());
        return "manageProducts";
    }

    @GetMapping("/cart")
    public String showCart(Model model) {
        User user = userService.findUserById(1L).orElseThrow();
        model.addAttribute("cartItems", user.getUserProducts());
        return "cart";
    }

    @PostMapping("/cart/checkout")
    public String checkout(Model model) {
        User user = userService.findUserById(1L)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        if (user.getUserProducts().isEmpty()) {
            model.addAttribute("error", "No puedes finalizar la compra con el carrito vacío");
            return "cart"; // Remains in the cart
        }

        // Continues with the process if the cart has products
        userService.productsFromCartIntoOrder(user);
        model.addAttribute("message", "¡Pedido realizado correctamente!");

        return "redirect:/orders";
    }

}
