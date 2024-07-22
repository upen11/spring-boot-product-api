package com.securityexample.securityex.repository;

import com.securityexample.securityex.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findProductByName(String name);
    List<Product> findProductByCategory(String category);

    @Query("SELECT p FROM Product p ORDER BY p.price ASC")
    List<Product> findAllProductsByPriceAsc();

//    List<Product> findAllByOrderByPriceAsc(); // This method will generate a query to fetch all Product entities and sort them by the price field in ascending order.
    List<Product> findAllByOrderByPriceDesc();

}
