package com.securityexample.securityex.service;

import com.securityexample.securityex.entity.Product;
import com.securityexample.securityex.exception.ProductNotFoundException;
import com.securityexample.securityex.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository repository;

    public ProductServiceImpl(ProductRepository repository) {
        this.repository = repository;
    }

    public Optional<Product> getProductById(Long id) {
        logger.debug("Fetching product with id {}", id);
        return repository.findById(id);
    }

    public List<Product> getProductByName(String name) {
        logger.debug("Fetching product with name {}", name);
        return repository.findProductByName(name);
    }

    public List<Product> getProductsByCategory(String category) {
        logger.debug("Fetching products with category {}", category);
        return repository.findProductByCategory(category);
    }

    public List<Product> findAllProductsByPriceAsc() {
        logger.debug("Fetching products sorted by price ascending");
        return repository.findAllProductsByPriceAsc();
    }

    public List<Product> findAllProductsByPriceDesc() {
        logger.debug("Fetching products sorted by price descending");
        return repository.findAllByOrderByPriceDesc();
    }

    public List<Product> getAllProducts() {
        logger.debug("Fetching all products");
        return repository.findAll();
    }

    public Product saveProduct(Product product) {
        logger.debug("Saving new product with name {}", product.getName());
        return repository.save(product);
    }


    public void deleteProduct(Long id) {
        logger.debug("Deleting product by id {}", id);
        Optional<Product> product = repository.findById(id);
        if (product.isPresent()) {
            repository.deleteById(id);
        } else {
            String errorMessage = "Product with ID " + id + " not found";
            throw new ProductNotFoundException(errorMessage);
        }
    }

    @Override
    public Product updateProduct(Long id, Product product) {
        logger.debug("Updating product with id {}", id);
        Optional<Product> optionalProduct = repository.findById(id);
        if (optionalProduct.isPresent()) {
            Product existingProduct = optionalProduct.get();
            existingProduct.setName(product.getName());
            existingProduct.setCategory(product.getCategory());
            existingProduct.setPrice(product.getPrice());
            existingProduct.setDescription(product.getDescription());
            return repository.save(existingProduct);
        } else {
            return null;
        }
    }
}
