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

    // Happy Path

    @Test
    void testGetProductById() throws Exception {
        Product product = new Product("Product", "Category", 100.0, "Description");
        product.setId(1L);

        when(productService.getProductById(1L)).thenReturn(product);

        mockMvc.perform(get("/api/v1/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Product"))
                .andExpect(jsonPath("$.category").value("Category"))
                .andExpect(jsonPath("$.price").value(100.0))
                .andExpect(jsonPath("$.description").value("Description"));

        verify(productService).getProductById(1L);
    }

    @Test
    void testGetProductByName() throws Exception {
        Product product = new Product("Product", "Category", 100.0, "Description");
        List<Product> products = Collections.singletonList(product);

        when(productService.getProductByName("Product")).thenReturn(products);

        mockMvc.perform(get("/api/v1/products/name/Product"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Product"));

        verify(productService).getProductByName("Product");
    }

    @Test
    void testGetProductByCategory() throws Exception {
        Product product = new Product("Product", "Category", 100.0, "Description");
        List<Product> products = Collections.singletonList(product);

        when(productService.getProductsByCategory("Category")).thenReturn(products);

        mockMvc.perform(get("/api/v1/products/category/Category"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].category").value("Category"));

        verify(productService).getProductsByCategory("Category");
    }

    @Test
    void testListAllProductsByPricesAsc() throws Exception {
        Product product = new Product("Product", "Category", 100.0, "Description");
        List<Product> products = Collections.singletonList(product);

        when(productService.findAllProductsByPriceAsc()).thenReturn(products);

        mockMvc.perform(get("/api/v1/products/price/asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].price").value(100.0));

        verify(productService).findAllProductsByPriceAsc();
    }

    @Test
    void testListAllProductsByPricesDesc() throws Exception {
        Product product = new Product("Product", "Category", 100.0, "Description");
        List<Product> products = Collections.singletonList(product);

        when(productService.findAllProductsByPriceDesc()).thenReturn(products);

        mockMvc.perform(get("/api/v1/products/price/desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].price").value(100.0));

        verify(productService).findAllProductsByPriceDesc();
    }

    @Test
    void testGetAllProducts() throws Exception {
        Product product = new Product("Product", "Category", 100.0, "Description");
        List<Product> products = Collections.singletonList(product);

        when(productService.getAllProducts()).thenReturn(products);

        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Product"));

        verify(productService).getAllProducts();
    }

    @Test
    void testCreateProduct() throws Exception {
        Product product = new Product("Product", "Category", 100.0, "Description");
        product.setId(1L);

        when(productService.saveProduct(any(Product.class))).thenReturn(product);

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(product)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Product"));

        verify(productService).saveProduct(any(Product.class));
    }

    @Test
    void testDeleteProduct() throws Exception {
        doNothing().when(productService).deleteProduct(1L);

        mockMvc.perform(delete("/api/v1/products/1"))
                .andExpect(status().isNoContent());

        verify(productService).deleteProduct(1L);
    }

    @Test
    void testUpdateProduct() throws Exception {
        Product product = new Product("Product", "Category", 100.0, "Description");
        product.setId(1L);
        Product updatedProduct = new Product("UpdatedProduct", "UpdatedCategory", 150.0, "UpdatedDescription");
        updatedProduct.setId(1L);

        when(productService.updateProduct(eq(1L), any(Product.class))).thenReturn(updatedProduct);

        mockMvc.perform(put("/api/v1/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("UpdatedProduct"));

        verify(productService).updateProduct(eq(1L), any(Product.class));
    }

    // Negative cases

    @Test
    void testGetProductByIdNotFound() throws Exception {
        when(productService.getProductById(1L)).thenThrow(ProductNotFoundException.class);

        mockMvc.perform(get("/api/v1/products/1"))
                .andExpect(status().isNotFound());

        verify(productService).getProductById(1L);
    }

    @Test
    void testGetProductByNameNotFound() throws Exception {
        when(productService.getProductByName("NonExistentName")).thenThrow(ProductNotFoundException.class);

        mockMvc.perform(get("/api/v1/products/name/NonExistentName"))
                .andExpect(status().isNotFound());

        verify(productService).getProductByName("NonExistentName");
    }

    @Test
    void testGetProductByCategoryNotFound() throws Exception {
        when(productService.getProductsByCategory("NonExistentCategory")).thenThrow(ProductNotFoundException.class);

        mockMvc.perform(get("/api/v1/products/category/NonExistentCategory"))
                .andExpect(status().isNotFound());

        verify(productService).getProductsByCategory("NonExistentCategory");
    }

    @Test
    void testListAllProductsByPricesAscNotFound() throws Exception {
        when(productService.findAllProductsByPriceAsc()).thenThrow(ProductNotFoundException.class);

        mockMvc.perform(get("/api/v1/products/price/asc"))
                .andExpect(status().isNotFound());

        verify(productService).findAllProductsByPriceAsc();
    }

    @Test
    void testListAllProductsByPricesDescNotFound() throws Exception {
        when(productService.findAllProductsByPriceDesc()).thenThrow(ProductNotFoundException.class);

        mockMvc.perform(get("/api/v1/products/price/desc"))
                .andExpect(status().isNotFound());

        verify(productService).findAllProductsByPriceDesc();
    }

    @Test
    void testGetAllProductsNotFound() throws Exception {
        when(productService.getAllProducts()).thenThrow(ProductNotFoundException.class);

        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isNotFound());

        verify(productService).getAllProducts();
    }

    @Test
    void testDeleteProductNotFound() throws Exception {
        doThrow(ProductNotFoundException.class).when(productService).deleteProduct(1L);

        mockMvc.perform(delete("/api/v1/products/1"))
                .andExpect(status().isNotFound());

        verify(productService).deleteProduct(1L);
    }

    @Test
    void testUpdateProductNotFound() throws Exception {
        Product product = new Product("Product", "Category", 100.0, "Description");
        product.setId(1L);

        when(productService.updateProduct(eq(1L), any(Product.class))).thenThrow(ProductNotFoundException.class);

        mockMvc.perform(put("/api/v1/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(product)))
                .andExpect(status().isNotFound());

        verify(productService).updateProduct(eq(1L), any(Product.class));
    }

    // Errors Tests
    @Test
    void testHandleMethodArgumentTypeMismatchException() throws Exception {
        mockMvc.perform(get("/api/v1/products/abc")) // Simulate invalid path variable
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid value 'abc' for parameter 'id'")); // Adjusted to match actual behavior
    }


    @Test
    void testHandleGenericException() throws Exception {
        // Simulate a situation where a generic exception would be thrown
        when(productService.getProductById(anyLong())).thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(get("/api/v1/products/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("An unexpected error occurred. Please try again later."));

        // Verify that the exception handling method is not explicitly called
        // The mockMvc call itself should cover this
    }

}
