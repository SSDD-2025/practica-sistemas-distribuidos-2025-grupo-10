package com.example.demo.controller;

import com.example.demo.model.Category;
import com.example.demo.model.Product;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.CategoryService;
import com.example.demo.service.ProductService;
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
import java.util.Collection;
import java.util.List;
import java.util.Optional;

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
        productService.deleteProductById(id);
        return "redirect:/products/manage"; // Recarga la página con la lista actualizada
    }

    @PostMapping("/products/{id}/update")
    public String updateProduct(@PathVariable long id,
                                @RequestParam String name,
                                @RequestParam BigDecimal price,
                                @RequestParam(required = false) Long categoryId, MultipartFile imageField) throws IOException{
        Optional<Product> existingProduct = productService.findProductById(id);

        if (existingProduct.isPresent()) {
            Product product = existingProduct.get();
            product.setName(name);
            product.setPrice(price);

            // Si categoryId no es nulo, buscamos la categoría; si es nulo, no asignamos ninguna
            if (categoryId != null) {
                Optional<Category> category = categoryRepository.findById(categoryId);
                category.ifPresent(product::setCategory);
            } else {
                product.setCategory(null); // Si el usuario no selecciona una categoría, se deja en null
            }

            productService.save(product, imageField);
        }
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
}
