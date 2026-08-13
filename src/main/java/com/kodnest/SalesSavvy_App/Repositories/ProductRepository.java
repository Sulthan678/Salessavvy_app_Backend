package com.kodnest.SalesSavvy_App.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.kodnest.SalesSavvy_App.Entities.Product;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByCategory_CategoryId(Integer categoryId);
    
    @Query("SELECT p.category.categoryName FROM Product p WHERE p.productId = :productId")
    String findCategoryNameByProductId(int productId);
    

    // SEARCH PRODUCTS
    List<Product> findByNameContainingIgnoreCase(String keyword);
    
    
 // SEARCH SUGGESTIONS
    List<Product> findTop5ByNameContainingIgnoreCase(String keyword);
    
    
//    SELECT * FROM product WHERE category = ? AND product_id != ?LIMIT 4;
    List<Product> findTop4ByCategory_CategoryNameAndProductIdNot(
    		String categoryName,
    		Integer productId
    		);
}

