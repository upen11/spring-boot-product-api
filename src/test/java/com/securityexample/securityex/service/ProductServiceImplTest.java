package com.securityexample.securityex.service;

import com.securityexample.securityex.entity.Product;
import com.securityexample.securityex.exception.ProductNotFoundException;
import com.securityexample.securityex.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
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

    // Happy path
    @Test
    void testGetProductById() {
        Product product = new Product("Product", "Category", 100.0, "Description");
        product.setId(1L);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Optional<Product> foundProduct = productService.getProductById(1L);

        assertTrue(foundProduct.isPresent());
        assertEquals("Product", foundProduct.get().getName());
    }

    @Test
    void testGetProductByName() {
        Product product = new Product("Product", "Category", 100.0, "Description");
        List<Product> products = List.of(product);

        when(productRepository.findProductByName("Product")).thenReturn(products);

        List<Product> foundProducts = productService.getProductByName("Product");

        assertEquals(1, foundProducts.size());
        assertEquals("Product", foundProducts.get(0).getName());
    }

    @Test
    void testGetProductsByCategory() {
        Product product = new Product("Product", "Category", 100.0, "Description");
        List<Product> products = List.of(product);

        when(productRepository.findProductByCategory("Category")).thenReturn(products);

        List<Product> foundProducts = productService.getProductsByCategory("Category");

        assertEquals(1, foundProducts.size());
        assertEquals("Category", foundProducts.get(0).getCategory());
    }

    @Test
    void testFindAllProductsByPriceAsc() {
        Product product1 = new Product("Product1", "Category", 50.0, "Description1");
        Product product2 = new Product("Product2", "Category", 100.0, "Description2");
        List<Product> products = Arrays.asList(product1, product2);

        when(productRepository.findAllProductsByPriceAsc()).thenReturn(products);

        List<Product> foundProducts = productService.findAllProductsByPriceAsc();

        assertEquals(2, foundProducts.size());
        assertEquals(50.0, foundProducts.get(0).getPrice());
        assertEquals(100.0, foundProducts.get(1).getPrice());
    }

    @Test
    void testFindAllProductsByPriceDesc() {
        Product product1 = new Product("Product1", "Category", 100.0, "Description1");
        Product product2 = new Product("Product2", "Category", 50.0, "Description2");
        List<Product> products = Arrays.asList(product1, product2);

        when(productRepository.findAllByOrderByPriceDesc()).thenReturn(products);

        List<Product> foundProducts = productService.findAllProductsByPriceDesc();

        assertEquals(2, foundProducts.size());
        assertEquals(100.0, foundProducts.get(0).getPrice());
        assertEquals(50.0, foundProducts.get(1).getPrice());
    }

    @Test
    void testGetAllProducts() {
        Product product = new Product("Product", "Category", 100.0, "Description");
        List<Product> products = List.of(product);

        when(productRepository.findAll()).thenReturn(products);

        List<Product> foundProducts = productService.getAllProducts();

        assertEquals(1, foundProducts.size());
        assertEquals("Product", foundProducts.get(0).getName());
    }

    @Test
    void testSaveProduct() {
        Product product = new Product("Product", "Category", 100.0, "Description");

        when(productRepository.save(product)).thenReturn(product);

        Product savedProduct = productService.saveProduct(product);

        assertEquals("Product", savedProduct.getName());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testUpdateProduct() {
        Product existingProduct = new Product("Old Product", "Category", 100.0, "Description");
        existingProduct.setId(1L);
        Product updatedProduct = new Product("New Product", "Category", 200.0, "Updated Description");
        updatedProduct.setId(1L);

        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(existingProduct)).thenReturn(existingProduct);

        Product result = productService.updateProduct(1L, updatedProduct);

        assertNotNull(result);
        assertEquals("New Product", result.getName());
        assertEquals(200.0, result.getPrice());
    }

    @Test
    void testDeleteProduct() {
        Product product = new Product("Product", "Category", 100.0, "Description");
        product.setId(1L);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        productService.deleteProduct(1L);

        verify(productRepository, times(1)).deleteById(1L);
    }

    // Negative cases

    @Test
    void testGetProductByIdNotFound() {
        // Test scenario where the product ID does not exist
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Product> foundProduct = productService.getProductById(1L);

        assertFalse(foundProduct.isPresent(), "Product should not be found");
    }

    @Test
    void testGetProductByNameNotFound() {
        // Test scenario where no products match the given name
        when(productRepository.findProductByName("NonExistentName")).thenReturn(Collections.emptyList());

        List<Product> foundProducts = productService.getProductByName("NonExistentName");

        assertTrue(foundProducts.isEmpty(), "Product list should be empty");
    }

    @Test
    void testGetProductsByCategoryNotFound() {
        // Test scenario where no products match the given category
        when(productRepository.findProductByCategory("NonExistentCategory")).thenReturn(Collections.emptyList());

        List<Product> foundProducts = productService.getProductsByCategory("NonExistentCategory");

        assertTrue(foundProducts.isEmpty(), "Product list should be empty");
    }

    @Test
    void testFindAllProductsByPriceAscEmptyList() {
        // Test scenario where no products are found
        when(productRepository.findAllProductsByPriceAsc()).thenReturn(Collections.emptyList());

        List<Product> foundProducts = productService.findAllProductsByPriceAsc();

        assertTrue(foundProducts.isEmpty(), "Product list should be empty");
    }

    @Test
    void testFindAllProductsByPriceDescEmptyList() {
        // Test scenario where no products are found
        when(productRepository.findAllByOrderByPriceDesc()).thenReturn(Collections.emptyList());

        List<Product> foundProducts = productService.findAllProductsByPriceDesc();

        assertTrue(foundProducts.isEmpty(), "Product list should be empty");
    }

    @Test
    void testUpdateProductNotFound() {
        // Test scenario where the product to update does not exist
        Product updatedProduct = new Product("New Product", "Category", 200.0, "Updated Description");
        updatedProduct.setId(1L);

        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        Product result = productService.updateProduct(1L, updatedProduct);

        assertNull(result, "Product should not be updated as it does not exist");
    }

    @Test
    void testDeleteProductNotFound() {
        // Test scenario where the product to delete does not exist
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct(1L));
    }
}
