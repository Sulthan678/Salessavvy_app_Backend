package com.kodnest.SalesSavvy_App.Controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.kodnest.SalesSavvy_App.Entities.User;
import com.kodnest.SalesSavvy_App.Services.WishlistService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/wishlist")
// @CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    // Toggle Wishlist (Add / Remove)
    @PostMapping("/toggle/{productId}")
    public ResponseEntity<Boolean> toggleWishlist(
            @PathVariable int productId,
            HttpServletRequest request) {

        User user = (User) request.getAttribute("authenticatedUser");

        boolean added = wishlistService.toggleWishlist(
                user.getUserId(),
                productId
        );

        return ResponseEntity.ok(added);
    }

    // Get Wishlist Items
    @GetMapping
    public ResponseEntity<Map<String, Object>> getWishlist(
            HttpServletRequest request) {

        User user = (User) request.getAttribute("authenticatedUser");

        return ResponseEntity.ok(
                wishlistService.getWishlistItems(user.getUserId())
        );
    }

    // Wishlist Count
    @GetMapping("/count")
    public ResponseEntity<Integer> getWishlistCount(
            HttpServletRequest request) {

        User user = (User) request.getAttribute("authenticatedUser");

        return ResponseEntity.ok(
                wishlistService.getWishlistCount(user.getUserId())
        );
    }

    // Check Product Exists
    @GetMapping("/check/{productId}")
    public ResponseEntity<Boolean> checkWishlist(
            @PathVariable int productId,
            HttpServletRequest request) {

        User user = (User) request.getAttribute("authenticatedUser");

        return ResponseEntity.ok(
                wishlistService.isProductInWishlist(
                        user.getUserId(),
                        productId
                )
        );
    }

}
