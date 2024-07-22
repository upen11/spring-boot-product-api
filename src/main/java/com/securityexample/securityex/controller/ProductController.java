package com.securityexample.securityex.controller;

import com.securityexample.securityex.entity.Product;
import com.securityexample.securityex.service.ProductServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
        Optional<Product> product = service.getProductById(id);
        if (product.isPresent()) {
            logger.info("Successfully fetched products with id {}", id);
            return ResponseEntity.ok(product.get());
        } else {
            logger.warn("Product with id {} not found", id);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<Product>> getProductByName(@PathVariable String name) {
        logger.info("Received request to fetch product with name {}", name);
        List<Product> products = service.getProductByName(name);
        if (products.isEmpty()) {
            logger.warn("No products found with name {}", name);
            return ResponseEntity.notFound().build();
        }
        logger.info("Successfully fetched products with name {}", name);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> getProductByCategory(@PathVariable String category) {
        logger.info("Received request to fetch product with category {}", category);
        List<Product> products = service.getProductsByCategory(category);
        if (products.isEmpty()) {
            logger.warn("No products found in category {}", category);
            return ResponseEntity.notFound().build();
        }
        logger.info("Successfully fetched products with category {}", category);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/price/asc")
    public ResponseEntity<List<Product>> listAllProductsByPricesAsc() {
        logger.info("Received request to fetch product with price ascending");
        List<Product> products = service.findAllProductsByPriceAsc();
        if (products.isEmpty()) {
            logger.warn("No products found ordered by price ascending");
            return ResponseEntity.notFound().build();
        }
        logger.info("Successfully fetched products with price ascending");
        return ResponseEntity.ok(products);
    }

    @GetMapping("/price/desc")
    public ResponseEntity<List<Product>> listAllProductsByPricesDesc() {
        logger.info("Received request to fetch product with price descending");
        List<Product> products = service.findAllProductsByPriceDesc();
        if (products.isEmpty()) {
            logger.warn("No products found ordered by price descending");
            return ResponseEntity.notFound().build();
        }
        logger.info("Successfully fetched products with price descending");
        return ResponseEntity.ok(products);
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        logger.info("Received request to fetch all products");
        List<Product> products = service.getAllProducts();
        if (products.isEmpty()) {
            logger.warn("No products found");
            return ResponseEntity.notFound().build();
        }
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
        if (updatedProduct != null) {
            logger.info("Successfully updated product with id {}", id);
            return ResponseEntity.ok(updatedProduct);
        } else {
            logger.warn("Product with id {} not found to update", id);
            return ResponseEntity.notFound().build();
        }
    }
}
