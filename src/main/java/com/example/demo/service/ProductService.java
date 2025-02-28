package com.example.demo.service;

import com.example.demo.model.Category;
import com.example.demo.model.Order;
import com.example.demo.model.Product;
import com.example.demo.model.User;
import com.example.demo.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Optional;
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final OrderService orderService;
    private final UserService userService;


    public ProductService(ProductRepository productRepository, OrderService orderService, UserService userService){
        this.productRepository = productRepository;
        this.orderService = orderService;
        this.userService = userService;
    }
    public void save(Product product){
        productRepository.save(product);
    }
    public Collection<Product> findall(){
        return productRepository.findAll();
    }
    public Optional<Product> findProductById(Long id){
        return productRepository.findById(id);
    }
    public void deleteProductById(Long id){
        productRepository.deleteById(id);
    }

    public void updateProduct(Product oldProduct, Product updatedProduct) {
        oldProduct.setName(updatedProduct.getName());
        oldProduct.setPrice(updatedProduct.getPrice());
        oldProduct.setCategory(updatedProduct.getCategory());
        productRepository.save(oldProduct);
    }

    public void initializeData(){
        productRepository.save(new Product());
        productRepository.save(new Product());
        productRepository.save(new Product());

        // Obtener y mostrar todas las categorías
        List<Product> categories = productRepository.findAll();
        System.out.println("Categories found with findAll():");
        System.out.println("--------------------------------");
        for (Product category : categories) {
            System.out.println(category);
        }
        System.out.println();

        // Buscar una categoría por ID
        Optional<Product> foundCategory = productRepository.findById(2L);
        if (foundCategory.isPresent()) {
            System.out.println("Category found with findById(2L):");
            System.out.println("--------------------------------");
            System.out.println(foundCategory.get());
            System.out.println();
        }

        // Verificar si existe una categoría con ID 4
        boolean exists = productRepository.existsById(4L);
        System.out.println("Does category with ID 4 exist? " + exists);
        System.out.println();

        // Buscar categorías por nombre
        List<Product> shrekCategories = productRepository.findByName("Shrek");
        System.out.println("Categories found with findByName('Shrek'):");
        System.out.println("------------------------------------------");
        for (Product category : shrekCategories) {
            System.out.println(category);
        }
        System.out.println();

        System.out.println("Eliminar si existe el id2");
        System.out.println("------------------------------------------");
        // Eliminar categoría con ID 2 si existe
        foundCategory.ifPresent(category -> productRepository.deleteById(2L));
        System.out.println();

        System.out.println("Eliminar en base a una clave no primaria -> name");
        System.out.println("------------------------------------------");
        List<Product> categoriesTwo = productRepository.findByName("Hola");
        productRepository.deleteAll(categoriesTwo);
        System.out.println();


        // Mostrar todas las categorías después de las modificaciones
        categories = productRepository.findAll();
        System.out.println("Categories after modifications:");
        System.out.println("(Deberia quedar solo 1 -> bocadillo de panceta)");
        System.out.println("-------------------------------");
        for (Product category : categories) {
            System.out.println(category);
        }
        System.out.println();

        System.out.println("Operaciones de modificacion");
        System.out.println("-------------------------------");
        List<Product> categoriesThree = productRepository.findByName("Bocadillo de panceta");
        Product first = categoriesThree.get(0);
        first.setName("Bocadillo cambiado");
        productRepository.save(first);
        System.out.println("Cambio: " + first.toString());
        System.out.println();

        System.out.println("Consulta por nombre cambiado");
        System.out.println("-------------------------------");
        List<Product> categoriesFour = productRepository.findByName("Bocadillo cambiado");
        for (Product category : categoriesFour) {
            System.out.println(category);
        }
    }

    public void saveProductCategory(Product productToSave, Category category) {
        productToSave.setCategory(category);
        this.save(productToSave);
    }

    //  Eliminar producto
    @Transactional
    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        // Eliminar el producto de todos los pedidos antes de borrarlo
        for (Order order : product.getOrders()) {
            order.getProducts().remove(product);
            orderService.save(order); // Guardar el pedido sin el producto
        }

        for (User u : userService.findAll()) {
            if (u.getUserProducts().contains(product)){
                u.getUserProducts().remove(product);
                userService.save(u);
            }
        }

        // Ahora eliminar el producto
        productRepository.delete(product);
    }
}
