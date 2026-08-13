package com.kodnest.SalesSavvy_App.Services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kodnest.SalesSavvy_App.Entities.Product;
import com.kodnest.SalesSavvy_App.Entities.ProductImage;
import com.kodnest.SalesSavvy_App.Entities.User;
import com.kodnest.SalesSavvy_App.Entities.Wishlist;
import com.kodnest.SalesSavvy_App.Repositories.ProductImageRepository;
import com.kodnest.SalesSavvy_App.Repositories.ProductRepository;
import com.kodnest.SalesSavvy_App.Repositories.UserRepository;
import com.kodnest.SalesSavvy_App.Repositories.WishlistRepository;

@Service
public class WishlistService {

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

    // Toggle Wishlist (Add if absent, Remove if present)
    public boolean toggleWishlist(int userId, int productId) {

        Optional<Wishlist> existingWishlist =
                wishlistRepository.findByUserAndProduct(userId, productId);

        if (existingWishlist.isPresent()) {

            wishlistRepository.deleteWishlistItem(userId, productId);
            return false; // Removed

        }

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Product not found"));

        Wishlist wishlist = new Wishlist(user, product);

        wishlistRepository.save(wishlist);

        return true; // Added
    }

    // Check if a product exists in wishlist
    public boolean isProductInWishlist(int userId, int productId) {

        return wishlistRepository
                .findByUserAndProduct(userId, productId)
                .isPresent();
    }

    // Count wishlist items
    public int getWishlistCount(int userId) {

        return wishlistRepository.countWishlistItems(userId);
    }

    // Fetch Wishlist
    public Map<String, Object> getWishlistItems(int userId) {

        List<Wishlist> wishlistItems =
                wishlistRepository.findWishlistItemsWithProductDetails(userId);

        Map<String, Object> response = new HashMap<>();

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found"));

        response.put("username", user.getUsername());
        response.put("role", user.getRole().toString());

        List<Map<String, Object>> products = new ArrayList<>();

        for (Wishlist wishlist : wishlistItems) {

            Product product = wishlist.getProduct();

            List<ProductImage> images =
                    productImageRepository.findByProduct_ProductId(
                            product.getProductId());

            String imageUrl = null;

            if (images != null && !images.isEmpty()) {
                imageUrl = images.get(0).getImageUrl();
            }

            Map<String, Object> productDetails = new HashMap<>();

            productDetails.put("product_id", product.getProductId());
            productDetails.put("name", product.getName());
            productDetails.put("description", product.getDescription());
            productDetails.put("price", product.getPrice());
            productDetails.put("image_url", imageUrl);

            products.add(productDetails);
        }

        response.put("wishlist", products);

        return response;
    }

}
