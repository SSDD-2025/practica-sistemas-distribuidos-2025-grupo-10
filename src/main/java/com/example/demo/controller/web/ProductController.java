package com.example.demo.controller.web;

import com.example.demo.dto.CategoryDTO;
import com.example.demo.dto.NewProductRequestDTO;
import com.example.demo.dto.ProductDTO;
import com.example.demo.model.Category;
import com.example.demo.model.Product;
import com.example.demo.model.User;
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
import java.security.Principal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Collection;
import java.util.NoSuchElementException;
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
    public String addProduct(@ModelAttribute Product product, MultipartFile imageField,
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
    private ProductDTO createOrReplaceProduct(NewProductRequestDTO newProductRequestDTO, long productId, Boolean removeImage) throws IOException, SQLException{
        boolean image = false;
        if(productId != 0){
            ProductDTO oldProduct = productService.findProductById(productId);
            image = removeImage ? false : oldProduct.image();
        }
        CategoryDTO categoryDTO = null;
        if(newProductRequestDTO.category() != null){
            categoryDTO = newProductRequestDTO.category();
        }
        ProductDTO productDTO = new ProductDTO(productId, newProductRequestDTO.name(), newProductRequestDTO.price(), image, categoryDTO);
        ProductDTO newProductDTO = productService.createOrReplaceProduct(productId, productDTO);
        MultipartFile imageField = newProductRequestDTO.imagefield();
        if(!imageField.isEmpty()){
            productService.createProductImage(productDTO.id(), imageField.getInputStream(), imageField.getSize());
        }
        return newProductDTO;
    }

     */


    @GetMapping("products/{id}/image") //no funciona
    public ResponseEntity<Object> downloadImage(@PathVariable long id) throws SQLException {

        Resource bookImage = productService.getProductImage(id);

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                .body(bookImage);
    }
    /*
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
        @GetMapping("/products/{id}")
        public String showProduct(Model model, @PathVariable long id) {
            Optional<Product> product = productService.findProductById(id);
            model.addAttribute("product", product);
            return "showProduct";
        }
    */
    @PostMapping("/products/{id}/delete")
    public String deleteProduct(Model model,@PathVariable long id) {
        try{
            ProductDTO productDTO = productService.deleteProduct(id);
            model.addAttribute("product", productDTO);
            return "redirect:/products/manage";
        } catch (NoSuchElementException e){
            return "ProductNotFound";
        }
    }
    /*
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

        // It only updates the image if the field is not empty
        if (imageField != null && !imageField.isEmpty()) {
            product.setImageFile(BlobProxy.generateProxy(imageField.getInputStream(), imageField.getSize()));
        }

        productService.save(product, categoryId);
        return "redirect:/products";
    }

     */
    /*
    @GetMapping("/products/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Product> product = productService.findProductById(id);
        if (product.isPresent()) {
            Product existingProduct = product.get();

            // If the category is null we don't access to its ID
            Long categoryId = (existingProduct.getCategory() != null) ? existingProduct.getCategory().getId() : null;

            model.addAttribute("product", existingProduct);
            model.addAttribute("categoryId", categoryId);
            model.addAttribute("categories", categoryService.findAll());

            return "editProduct";
        }
        return "redirect:/products";
    }

     */

    @GetMapping("/products/manage")
    public String showManageProducts(Model model) {
        model.addAttribute("products", productService.findall());
        return "manageProducts";
    }

    @GetMapping("/cart")
    public String showCart(Model model, Principal principal) {
        if (principal == null) return "redirect:/login";

        String username = principal.getName();
        User user = userService.findByUsername(username);

        model.addAttribute("cartItems", user.getUserProducts());
        return "cart";
    }
    /* GESTIONAR EN CUANTO SE PONGA LA RELACION USER-PRODUCT
    @PostMapping("/cart/add/{id}")
    public String addProductTocart(@PathVariable long id, Principal principal) throws IOException {
        if (principal == null) return "redirect:/login";

        String username = principal.getName();
        User user = userService.findByUsername(username);

        Product product = productService.findProductById(id).orElseThrow();
        userService.addProductToCart(product, user);

        return "redirect:/cart";
    }

     */

    @PostMapping("/cart/checkout")
    public String checkout(Model model, Principal principal) {
        if (principal == null) return "redirect:/login";

        String username = principal.getName();
        User user = userService.findByUsername(username);

        if (user.getUserProducts().isEmpty()) {
            model.addAttribute("error", "No puedes finalizar la compra con el carrito vacío");
            return "cart";
        }

        userService.productsFromCartIntoOrder(user);
        model.addAttribute("message", "¡Pedido realizado correctamente!");

        return "redirect:/orders";
    }


}
