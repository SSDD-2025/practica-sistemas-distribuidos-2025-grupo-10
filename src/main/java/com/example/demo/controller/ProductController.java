package com.example.demo.controller;

import com.example.demo.model.Category;
import com.example.demo.model.Order;
import com.example.demo.model.Product;
import com.example.demo.model.User;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.CategoryService;
import com.example.demo.service.ProductService;
import com.example.demo.service.UserService;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.*;

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CategoryRepository categoryRepository;
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

        // Agregar los productos al modelo para que Mustache los reconozca
        model.addAttribute("products", products);
        return "products";
    }


    @GetMapping("/manageProducts")
    public String showManageProductsPage(Model model) {
        model.addAttribute("products", productService.findall());
        return "manageProducts";
    }
    @GetMapping("products/add")
    public String showFormAdd(Model model){
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
    public String addProductTocart(@PathVariable long id) throws IOException{
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
    public String showProduct(Model model, @PathVariable long id){
        Optional<Product> product = productService.findProductById(id);
        model.addAttribute("product", product);
        return "showProduct";
    }

    /*@GetMapping("/products/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Product> product = productService.findProductById(id);
        if (product.isPresent()) {
            model.addAttribute("product", product.get());
            return "manageProduct"; // Apunta al archivo correcto
        }
        return "redirect:/products";
    }*/
    @PostMapping("/products/{id}/delete")
    public String deleteProduct(@PathVariable long id) {
        productService.deleteProduct(id);
        return "redirect:/products/manage"; // Recarga la página con la lista actualizada
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

        // Solo actualiza la imagen si el campo no está vacío
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

            // Si la categoría es null, no intentamos acceder a su ID
            Long categoryId = (existingProduct.getCategory() != null) ? existingProduct.getCategory().getId() : null;

            model.addAttribute("product", existingProduct);
            model.addAttribute("categoryId", categoryId);
            model.addAttribute("categories", categoryRepository.findAll()); // Cargar todas las categorías en el formulario

            return "editProduct"; // Asegúrate de que el archivo HTML correcto se llame así
        }
        return "redirect:/products";
    }

    @GetMapping("/products/manage")
    public String showManageProducts(Model model) {
        model.addAttribute("products", productService.findall());
        return "manageProducts"; // Busca el archivo en templates/manageProducts.html
    }

    @GetMapping("/cart")
    public String showCart(Model model) {
        User user = userService.findUserById(1L).orElseThrow();
        model.addAttribute("cartItems", user.getUserProducts());
        return "cart";  // Muestra el carrito con los productos añadidos
    }

    @PostMapping("/cart/checkout")
    public String checkout(Model model) {
        User user = userService.findUserById(1L)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        List<Product> cartItems = user.getUserProducts();

        if (cartItems.isEmpty()) {
            model.addAttribute("message", "No puedes finalizar compra con el carrito vacío.");
            return "orderConfirmation";
        }

        BigDecimal total = cartItems.stream()
                .map(Product::getPrice)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = new Order();
        order.setTotal(total);
        order.setNumItems(cartItems.size());
        order.setDate(new Date());
        order.setStatus("Realizado"); // Estado fijo para simplificar

        orderRepository.save(order); // Guarda el pedido

        // Vaciar carrito
        user.getUserProducts().clear();
        userService.save(user);

        model.addAttribute("message", "¡Pedido realizado correctamente!");
        return "orderConfirmation";
    }



}
