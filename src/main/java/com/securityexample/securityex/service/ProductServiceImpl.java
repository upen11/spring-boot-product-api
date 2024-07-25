package com.securityexample.securityex.service;

import com.securityexample.securityex.entity.Product;
import com.securityexample.securityex.exception.ProductNotFoundException;
import com.securityexample.securityex.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository repository;

    public ProductServiceImpl(ProductRepository repository) {
        this.repository = repository;
    }

    public Product getProductById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid product ID: " + id);
        }
        logger.debug("Fetching product with id {}", id);
        return repository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product with id " + id + " was not found"));
    }

    public List<Product> getProductByName(String name) {
        logger.debug("Fetching product with name {}", name);
        List<Product> products = repository.findProductByName(name);
        if (products.isEmpty()) {
            throw new ProductNotFoundException("No products found with name " + name);
        }
        return products;
    }

    public List<Product> getProductsByCategory(String category) {
        logger.debug("Fetching products with category {}", category);
        List<Product> products = repository.findProductByCategory(category);
        if (products.isEmpty()) {
            throw new ProductNotFoundException("No products found in category " + category);
        }
        return products;
    }

    public List<Product> findAllProductsByPriceAsc() {
        logger.debug("Fetching products sorted by price ascending");
        List<Product> products = repository.findAllProductsByPriceAsc();
        if (products.isEmpty()) {
            throw new ProductNotFoundException("No products found ordered by price ascending");
        }
        return products;
    }

    public List<Product> findAllProductsByPriceDesc() {
        logger.debug("Fetching products sorted by price descending");
        List<Product> products = repository.findAllByOrderByPriceDesc();
        if (products.isEmpty()) {
            throw new ProductNotFoundException("No products found ordered by price descending");
        }
        return products;
    }

    public List<Product> getAllProducts() {
        logger.debug("Fetching all products");
        List<Product> products = repository.findAll();
        if (products.isEmpty()) {
            throw new ProductNotFoundException("No products found");
        }
        return products;
    }

    public Product saveProduct(Product product) {
        logger.debug("Saving new product with name {}", product.getName());
        return repository.save(product);
    }

    public void deleteProduct(Long id) {
        // Reuse getProductById to handle validation and existence check
        logger.debug("Deleting product by id {}", id);
        getProductById(id);
        repository.deleteById(id);
    }


    public Product updateProduct(Long id, Product product) {
        // Reuse getProductById to handle validation and existence check
        Product existingProduct = getProductById(id);
        logger.debug("Updating product with id {}", id);
        existingProduct.setName(product.getName());
        existingProduct.setCategory(product.getCategory());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setDescription(product.getDescription());
        return repository.save(existingProduct);
    }
}
