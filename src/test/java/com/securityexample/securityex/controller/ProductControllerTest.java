package com.securityexample.securityex.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.securityexample.securityex.entity.Product;
import com.securityexample.securityex.exception.ProductNotFoundException;
import com.securityexample.securityex.service.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        // No need to manually initialize mocks with MockitoAnnotations.openMocks(this) here
    }

    @Test
    void testGetProductById() throws Exception {
        Product product = new Product("Product", "Category", 100.0, "Description");
        product.setId(1L);

        when(productService.getProductById(1L)).thenReturn(Optional.of(product));

        mockMvc.perform(get("/api/products/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Product"));
    }

    @Test
    void testGetProductByName() throws Exception {
        Product product = new Product("Product", "Category", 100.0, "Description");
        List<Product> products = List.of(product);

        when(productService.getProductByName("Product")).thenReturn(products);

        mockMvc.perform(get("/api/products/name/{name}", "Product"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Product"));
    }

    @Test
    void testGetProductByCategory() throws Exception {
        Product product = new Product("Product", "Category", 100.0, "Description");
        List<Product> products = List.of(product);

        when(productService.getProductsByCategory("Category")).thenReturn(products);

        mockMvc.perform(get("/api/products/category/{category}", "Category"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].category").value("Category"));
    }

    @Test
    void testListByPricesAsc() throws Exception {
        Product product = new Product("Product", "Category", 100.0, "Description");
        List<Product> products = List.of(product);

        when(productService.findAllProductsByPriceAsc()).thenReturn(products);

        mockMvc.perform(get("/api/products/price/asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].price").value(100.0));
    }

    @Test
    void testListByPricesDesc() throws Exception {
        Product product = new Product("Product", "Category", 100.0, "Description");
        List<Product> products = List.of(product);

        when(productService.findAllProductsByPriceDesc()).thenReturn(products);

        mockMvc.perform(get("/api/products/price/desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].price").value(100.0));
    }

    @Test
    void testGetAllProducts() throws Exception {
        Product product = new Product("Product", "Category", 100.0, "Description");
        List<Product> products = List.of(product);

        when(productService.getAllProducts()).thenReturn(products);

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Product"));
    }

    @Test
    void testCreateProduct() throws Exception {
        Product product = new Product("Product", "Category", 100.0, "Description");

        when(productService.saveProduct(any(Product.class))).thenReturn(product);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(product)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Product"));
    }

    @Test
    void testDeleteProductSuccess() throws Exception {
        // Test scenario where the product exists and is successfully deleted
        doNothing().when(productService).deleteProduct(1L); // Mock deleteProduct to do nothing

        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isNoContent()); // Expect 204 No Content
    }

    @Test
    void testUpdateProduct() throws Exception {
        Product existingProduct = new Product("Product", "Category", 100.0, "Description");
        existingProduct.setId(1L);
        Product updatedProduct = new Product("Updated Product", "Category", 200.0, "Updated Description");
        updatedProduct.setId(1L);

        when(productService.updateProduct(eq(1L), any(Product.class))).thenReturn(updatedProduct);

        mockMvc.perform(put("/api/products/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Product"));
    }

    // Negative Test Cases

    @Test
    void testGetAllProductsNotFound() throws Exception {
        // Test scenario where no products are found
        when(productService.getAllProducts()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetProductByIdNotFound() throws Exception {
        // Test scenario where the product ID does not exist
        when(productService.getProductById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetProductByNameNotFound() throws Exception {
        // Test scenario where no products match the given name
        when(productService.getProductByName("NonExistentName")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/products/name/NonExistentName"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetProductsByCategoryNotFound() throws Exception {
        // Test scenario where no products match the given category
        when(productService.getProductsByCategory("NonExistentCategory")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/products/category/NonExistentCategory"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testFindAllProductsByPriceAscEmptyList() throws Exception {
        // Test scenario where no products are available
        when(productService.findAllProductsByPriceAsc()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/products/price/asc"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testFindAllProductsByPriceDescEmptyList() throws Exception {
        // Test scenario where no products are available
        when(productService.findAllProductsByPriceDesc()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/products/price/desc"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateProductNotFound() throws Exception {
        // Test scenario where the product to update does not exist
        Product product = new Product("New Product", "Category", 200.0, "Updated Description");
        product.setId(1L);

        when(productService.updateProduct(1L, product)).thenReturn(null);

        mockMvc.perform(put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(product)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteProductNotFound() throws Exception {
        // Test scenario where the product to delete does not exist
        doThrow(new ProductNotFoundException("Product with ID 1 not found")).when(productService).deleteProduct(1L);

        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isNotFound()); // Expect 404 Not Found
    }
}
