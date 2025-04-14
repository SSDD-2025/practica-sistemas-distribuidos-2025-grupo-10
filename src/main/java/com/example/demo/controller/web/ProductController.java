package com.example.demo.controller.web;

import com.example.demo.dto.CategoryDTO;
import com.example.demo.dto.ProductDTO;
import com.example.demo.dto.ProductMapper;
import com.example.demo.dto.UserDTO;
import com.example.demo.model.Product;
import com.example.demo.model.User;
import com.example.demo.service.CategoryService;
import com.example.demo.service.ProductService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.Principal;
import java.sql.SQLException;
import java.util.NoSuchElementException;

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductMapper productMapper;

    @GetMapping("/products")
    public String showProducts(Model model) {
        model.addAttribute("products", productService.findall());
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

    @PostMapping("/products/add") //hacerlo desde newBookProcess
    public String addProduct(@ModelAttribute Product product,
                             MultipartFile imageField,
                             @RequestParam("categoryId") Long categoryId) throws IOException {

        productService.save(product, imageField, categoryId);
        return "redirect:/products/add?success=true";
    }

    /*
    @PostMapping("/products/add")
    public String newProductProcess(Model model, NewProductRequestDTO newProductRequestDTO) throws IOException, SQLException{
        createOrReplaceProduct(newProductRequestDTO, -1, null);
        return "redirect:/products/add?success=true";
    }
     */

    @GetMapping("products/{id}/image")
    public ResponseEntity<Object> downloadImage(@PathVariable long id) throws SQLException {

        Resource image = productService.getProductImage(id);

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                .body(image);
    }

    /*
    //  Este apartado no tiene funcionalidad, ni siquiera en la primera practica

    @GetMapping("/products/{id}/image") //Anterior pero no funciona por el optional
    public ResponseEntity<Object> downloadImage(@PathVariable long id) throws SQLException{
        Optional<Product> op = productService.findProductById(id);
        if(op.isPresent() && op.get().getImageFile() != null){
            Blob image = op.get().getImageFile();
            Resource file = new InputStreamResource(image.getBinaryStream());
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                    .contentLength(image.length()).body(file);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

     */
    /*
    //  Este apartado no tiene funcionalidad, ni siquiera en la primera practica
    @GetMapping("/products/{id}")
    public String showProduct(Model model, @PathVariable long id) {
        try {
            ProductDTO productById = productService.findProductById(id);
            Product product = productMapper.toDomain(productById);
            model.addAttribute("product", productById);
            return "showProduct";

        } catch (NoSuchElementException e) {
            return "ProductNotFound";
        }
    }

     */

    @PostMapping("/products/{id}/delete")
    public String deleteProduct(Model model, @PathVariable long id) {
        try {
            ProductDTO productDTO = productService.deleteProduct(id, userService);
            model.addAttribute("product", productDTO);
            return "redirect:/products/manage";
        } catch (NoSuchElementException e) {
            return "ProductNotFound";
        }
    }

    @PostMapping("/products/{id}/update")
    public String updateProduct(Model model,
                                @PathVariable long id,
                                @RequestParam String name,
                                @RequestParam BigDecimal price,
                                @RequestParam(required = false) Long categoryId,
                                @RequestParam(required = false) MultipartFile imageField) throws IOException {
        try {
            ProductDTO oldProductDTO = productService.findProductById(id);
            CategoryDTO categoryDTO = categoryService.findCategoryById(categoryId);
            ProductDTO updatedProductDTO = new ProductDTO(-1, name, price, oldProductDTO.image(), categoryDTO);
            productService.createOrReplaceProduct(id, updatedProductDTO);
        } catch (NoSuchElementException e) {
            return "ProductNotFound";
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/products";
    }


    @GetMapping("/products/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        try {
            ProductDTO productDTO = productService.findProductById(id);

            Long categoryId = (productDTO.category() != null)
                    ? productDTO.category().id()
                    : null;

            model.addAttribute("product", productDTO);
            model.addAttribute("categoryId", categoryId);
            model.addAttribute("categories", categoryService.findAll());

            return "editProduct";

        } catch (NoSuchElementException e) {
            return "redirect:/products";
        }
    }

    @GetMapping("/products/manage")
    public String showManageProducts(Model model) {
        model.addAttribute("products", productService.findall());
        return "manageProducts";
    }

    @GetMapping("/cart")
    public String showCart(Model model, Principal principal) {
        if (principal == null) return "redirect:/login";

        String username = principal.getName();
        UserDTO user = userService.findByUsername(username);

        model.addAttribute("cartItems", user.userProducts());
        return "cart";
    }

    //  GESTIONAR EN CUANTO SE PONGA LA RELACION USER-PRODUCT Creo que esta hecho,
    /*
    He probado con el usuario 1, a añadir cosas a su cesta, salirme
    Entrar con el usuario 2, y añadir cosas a su cesta, salirme
    Y comprobar de que sigue estando las cosas de la cesta 1, y hacer la compra
     */
    @PostMapping("/cart/add/{id}")
    public String addProductTocart(@PathVariable long id, Principal principal) throws IOException {
        if (principal == null) return "redirect:/login";

        String username = principal.getName();

        userService.addProductToCart2(id, username);

        return "redirect:/cart";
    }


    @PostMapping("/cart/checkout")
    public String checkout(Model model, Principal principal) {
        if (principal == null) return "redirect:/login";

        String username = principal.getName();
        UserDTO user = userService.findByUsername(username);

        if (user.userProducts().isEmpty()) {
            model.addAttribute("error", "No puedes finalizar la compra con el carrito vacío");
            return "cart";
        }

        userService.productsFromCartIntoOrder2(username);
        model.addAttribute("message", "¡Pedido realizado correctamente!");

        return "redirect:/orders";
    }

}
