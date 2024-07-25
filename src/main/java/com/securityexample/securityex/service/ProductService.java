package com.securityexample.securityex.service;

import com.securityexample.securityex.entity.Product;

import java.util.List;

public interface ProductService {
    Product getProductById(Long id);
    List<Product> getProductByName(String name);
    List<Product> getProductsByCategory(String category);
    List<Product> findAllProductsByPriceAsc();
    List<Product> findAllProductsByPriceDesc();
    List<Product> getAllProducts();
    Product saveProduct(Product product);
    void deleteProduct(Long id);
    Product updateProduct(Long id, Product product);
}
