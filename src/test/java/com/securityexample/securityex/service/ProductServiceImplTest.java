package com.securityexample.securityex.service;

import com.securityexample.securityex.entity.Product;
import com.securityexample.securityex.exception.ProductNotFoundException;
import com.securityexample.securityex.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetProductById() {
        Product product = new Product("Product", "Category", 100.0, "Description");
        product.setId(1L);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product foundProduct = productService.getProductById(1L);

        assertNotNull(foundProduct);
        assertEquals("Product", foundProduct.getName());
        verify(productRepository).findById(1L);
    }

    @Test
    void testGetProductByIdInvalidId() {
        assertThrows(IllegalArgumentException.class, () -> productService.getProductById(null));
        assertThrows(IllegalArgumentException.class, () -> productService.getProductById(0L));
    }

    @Test
    void testGetProductByIdNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(1L));
    }

    @Test
    void testGetProductByName() {
        Product product = new Product("Product", "Category", 100.0, "Description");
        List<Product> products = List.of(product);

        when(productRepository.findProductByName("Product")).thenReturn(products);

        List<Product> foundProducts = productService.getProductByName("Product");

        assertFalse(foundProducts.isEmpty());
        assertEquals("Product", foundProducts.get(0).getName());
    }

    @Test
    void testGetProductByNameNotFound() {
        when(productRepository.findProductByName("NonExistentName")).thenReturn(Collections.emptyList());

        assertThrows(ProductNotFoundException.class, () -> productService.getProductByName("NonExistentName"));
    }

    @Test
    void testGetProductsByCategory() {
        Product product = new Product("Product", "Category", 100.0, "Description");
        List<Product> products = List.of(product);

        when(productRepository.findProductByCategory("Category")).thenReturn(products);

        List<Product> foundProducts = productService.getProductsByCategory("Category");

        assertFalse(foundProducts.isEmpty());
        assertEquals("Category", foundProducts.get(0).getCategory());
    }

    @Test
    void testGetProductsByCategoryNotFound() {
        when(productRepository.findProductByCategory("NonExistentCategory")).thenReturn(Collections.emptyList());

        assertThrows(ProductNotFoundException.class, () -> productService.getProductsByCategory("NonExistentCategory"));
    }

    @Test
    void testFindAllProductsByPriceAsc() {
        Product product = new Product("Product", "Category", 100.0, "Description");
        List<Product> products = List.of(product);

        when(productRepository.findAllProductsByPriceAsc()).thenReturn(products);

        List<Product> foundProducts = productService.findAllProductsByPriceAsc();

        assertFalse(foundProducts.isEmpty());
        assertEquals(100.0, foundProducts.get(0).getPrice());
    }

    @Test
    void testFindAllProductsByPriceAscNotFound() {
        when(productRepository.findAllProductsByPriceAsc()).thenReturn(Collections.emptyList());

        assertThrows(ProductNotFoundException.class, () -> productService.findAllProductsByPriceAsc());
    }

    @Test
    void testFindAllProductsByPriceDesc() {
        Product product = new Product("Product", "Category", 100.0, "Description");
        List<Product> products = List.of(product);

        when(productRepository.findAllByOrderByPriceDesc()).thenReturn(products);

        List<Product> foundProducts = productService.findAllProductsByPriceDesc();

        assertFalse(foundProducts.isEmpty());
        assertEquals(100.0, foundProducts.get(0).getPrice());
    }

    @Test
    void testFindAllProductsByPriceDescNotFound() {
        when(productRepository.findAllByOrderByPriceDesc()).thenReturn(Collections.emptyList());

        assertThrows(ProductNotFoundException.class, () -> productService.findAllProductsByPriceDesc());
    }

    @Test
    void testGetAllProducts() {
        Product product = new Product("Product", "Category", 100.0, "Description");
        List<Product> products = List.of(product);

        when(productRepository.findAll()).thenReturn(products);

        List<Product> allProducts = productService.getAllProducts();

        assertFalse(allProducts.isEmpty());
        assertEquals("Product", allProducts.get(0).getName());
    }

    @Test
    void testGetAllProductsNotFound() {
        when(productRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(ProductNotFoundException.class, () -> productService.getAllProducts());
    }

    @Test
    void testSaveProduct() {
        Product product = new Product("Product", "Category", 100.0, "Description");

        when(productRepository.save(product)).thenReturn(product);

        Product savedProduct = productService.saveProduct(product);

        assertEquals("Product", savedProduct.getName());
        verify(productRepository).save(product);
    }

    @Test
    void testDeleteProduct() {
        Product product = new Product("Product", "Category", 100.0, "Description");
        product.setId(1L);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        doNothing().when(productRepository).deleteById(1L);

        productService.deleteProduct(1L);

        verify(productRepository).deleteById(1L);
    }

    @Test
    void testDeleteProductNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct(1L));
    }

    @Test
    void testUpdateProduct() {
        Product existingProduct = new Product("Product", "Category", 100.0, "Description");
        existingProduct.setId(1L);

        Product updatedProduct = new Product("Updated Product", "Updated Category", 200.0, "Updated Description");
        updatedProduct.setId(1L);

        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(existingProduct)).thenReturn(existingProduct);

        Product result = productService.updateProduct(1L, updatedProduct);

        assertEquals("Updated Product", result.getName());
        assertEquals("Updated Category", result.getCategory());
        assertEquals(200.0, result.getPrice());
        assertEquals("Updated Description", result.getDescription());
    }

    @Test
    void testUpdateProductNotFound() {
        Product product = new Product("Product", "Category", 100.0, "Description");
        product.setId(1L);

        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.updateProduct(1L, product));
    }
}
