package com.securityexample.securityex.controller;

import com.securityexample.securityex.entity.Product;
import com.securityexample.securityex.service.ProductServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private final ProductServiceImpl service;

    public ProductController(ProductServiceImpl service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        logger.info("Received request to fetch product with id {}", id);
        Product product = service.getProductById(id);
        logger.info("Successfully fetched product with id {}", id);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<Product>> getProductByName(@PathVariable String name) {
        logger.info("Received request to fetch products with name {}", name);
        List<Product> products = service.getProductByName(name);
        logger.info("Successfully fetched products with name {}", name);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> getProductByCategory(@PathVariable String category) {
        logger.info("Received request to fetch products with category {}", category);
        List<Product> products = service.getProductsByCategory(category);
        logger.info("Successfully fetched products with category {}", category);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/price/asc")
    public ResponseEntity<List<Product>> listAllProductsByPricesAsc() {
        logger.info("Received request to fetch products with price ascending");
        List<Product> products = service.findAllProductsByPriceAsc();
        logger.info("Successfully fetched products with price ascending");
        return ResponseEntity.ok(products);
    }

    @GetMapping("/price/desc")
    public ResponseEntity<List<Product>> listAllProductsByPricesDesc() {
        logger.info("Received request to fetch products with price descending");
        List<Product> products = service.findAllProductsByPriceDesc();
        logger.info("Successfully fetched products with price descending");
        return ResponseEntity.ok(products);
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        logger.info("Received request to fetch all products");
        List<Product> products = service.getAllProducts();
        logger.info("Successfully fetched all products");
        return ResponseEntity.ok(products);
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        logger.info("Received request to save product with name {}", product.getName());
        Product createdProduct = service.saveProduct(product);
        logger.info("Successfully added product with id {}", createdProduct.getId());
        return ResponseEntity.ok(createdProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        logger.info("Received request to delete product with id {}", id);
        service.deleteProduct(id);
        logger.info("Successfully deleted product with id {}", id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        logger.info("Received request to update product with id {}", id);
        Product updatedProduct = service.updateProduct(id, product);
        logger.info("Successfully updated product with id {}", id);
        return ResponseEntity.ok(updatedProduct);
    }
}