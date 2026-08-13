package com.kodnest.SalesSavvy_App.Controllers;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.kodnest.SalesSavvy_App.Entities.Product;
import com.kodnest.SalesSavvy_App.Entities.User;
import com.kodnest.SalesSavvy_App.Services.ProductService;

import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getProducts(
            @RequestParam(required = false) String category,
            HttpServletRequest request) {
        try {
            // Retrieve authenticated user from the request attribute set by the filter
            User authenticatedUser = (User) request.getAttribute("authenticatedUser");
            if (authenticatedUser == null) {
                return ResponseEntity.status(401).body(Map.of("error", "Unauthorized access"));
            }

            // Fetch products based on the category filter
            List<Product> products = productService.getProductsByCategory(category);

            // Build the response
            Map<String, Object> response = new HashMap<>();
            
            // Add user info
            Map<String, String> userInfo = new HashMap<>();
            userInfo.put("name", authenticatedUser.getUsername());
            userInfo.put("role", authenticatedUser.getRole().name());
            response.put("user", userInfo);

            // Add product details
            List<Map<String, Object>> productList = new ArrayList<>();
            for (Product product : products) {
                Map<String, Object> productDetails = new HashMap<>();
                productDetails.put("product_id", product.getProductId());
                productDetails.put("name", product.getName());
                productDetails.put("description", product.getDescription());
                productDetails.put("price", product.getPrice());
                productDetails.put("stock", product.getStock());

                // Fetch product images
                List<String> images = productService.getProductImages(product.getProductId());
                productDetails.put("images", images);

                productList.add(productDetails);
            }
            response.put("products", productList);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    // ==================================================
    // SEARCH PRODUCTS
    // ==================================================

    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchProducts(
            @RequestParam String keyword,
            HttpServletRequest request) {

        try {

            User authenticatedUser =
                    (User) request.getAttribute("authenticatedUser");

            if (authenticatedUser == null) {
                return ResponseEntity.status(401)
                        .body(Map.of("error", "Unauthorized access"));
            }

            List<Product> products =
                    productService.searchProducts(keyword);

            Map<String, Object> response = new HashMap<>();

            Map<String, String> userInfo = new HashMap<>();
            userInfo.put("name", authenticatedUser.getUsername());
            userInfo.put("role", authenticatedUser.getRole().name());

            response.put("user", userInfo);

            List<Map<String, Object>> productList =
                    new ArrayList<>();

            for (Product product : products) {

                Map<String, Object> productDetails =
                        new HashMap<>();

                productDetails.put("product_id",
                        product.getProductId());

                productDetails.put("name",
                        product.getName());

                productDetails.put("description",
                        product.getDescription());

                productDetails.put("price",
                        product.getPrice());

                productDetails.put("stock",
                        product.getStock());

                List<String> images =
                        productService.getProductImages(
                                product.getProductId());

                productDetails.put("images",
                        images);

                productList.add(productDetails);
            }

            response.put("products", productList);

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {

            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }
 // ==================================================
 // SEARCH SUGGESTIONS
 // ==================================================

		 @GetMapping("/suggestions")
		 public ResponseEntity<List<String>> getSuggestions(
		         @RequestParam String keyword,
		         HttpServletRequest request) {
		
		     User authenticatedUser =
		             (User) request.getAttribute("authenticatedUser");
		
		     if (authenticatedUser == null) {
		         return ResponseEntity.status(401).build();
		     }
		
		     List<String> suggestions =
		             productService.getSuggestions(keyword);
		
		     return ResponseEntity.ok(suggestions);
		 }
		 
		 
		// ==================================================
		// GET PRODUCT BY ID
		// ==================================================

		@GetMapping("/{productId}")
		public ResponseEntity<Map<String, Object>> getProductById(
		        @PathVariable Integer productId,
		        HttpServletRequest request) {

		    try {

		        User authenticatedUser =
		                (User) request.getAttribute("authenticatedUser");

		        if (authenticatedUser == null) {
		            return ResponseEntity.status(401)
		                    .body(Map.of("error", "Unauthorized access"));
		        }

		        Product product = productService.getProductById(productId);

		        Map<String, Object> response = new HashMap<>();

		        response.put("product_id", product.getProductId());
		        response.put("name", product.getName());
		        response.put("description", product.getDescription());
		        response.put("price", product.getPrice());
		        response.put("stock", product.getStock());
		        
		        response.put("category", product.getCategory().getCategoryName());

		        List<String> images =
		                productService.getProductImages(product.getProductId());

		        response.put("images", images);
		        response.put("user", Map.of(
		        	    "name", authenticatedUser.getUsername()));
		        
		        return ResponseEntity.ok(response);

		    } catch (RuntimeException e) {

		        return ResponseEntity.badRequest()
		                .body(Map.of("error", e.getMessage()));
		    }
		}
		
		@GetMapping("/{productId}/similar")
		public ResponseEntity<List<Map<String, Object>>> getSimilarProducts(
		        @PathVariable Integer productId,
		        HttpServletRequest request) {

		    try {

		        User authenticatedUser =
		                (User) request.getAttribute("authenticatedUser");

		        if (authenticatedUser == null) {
		            return ResponseEntity.status(401).build();
		        }

		        List<Product> similarProducts =
		                productService.getSimilarProducts(productId);

		        List<Map<String, Object>> response = new ArrayList<>();

		        for (Product product : similarProducts) {

		            Map<String, Object> productMap = new HashMap<>();

		            productMap.put("product_id", product.getProductId());
		            productMap.put("name", product.getName());
		            productMap.put("price", product.getPrice());
		            productMap.put("category",
		                    product.getCategory().getCategoryName());

		            List<String> images =
		                    productService.getProductImages(product.getProductId());

		            productMap.put("images", images);

		            response.add(productMap);
		        }

		        return ResponseEntity.ok(response);

		    } catch (RuntimeException e) {

		        return ResponseEntity.badRequest().build();

		    }

		}
}

