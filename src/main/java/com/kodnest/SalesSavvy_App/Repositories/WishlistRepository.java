package com.kodnest.SalesSavvy_App.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.kodnest.SalesSavvy_App.Entities.Wishlist;

import jakarta.transaction.Transactional;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Integer> {

    // Check if a product already exists in the user's wishlist
    @Query("""
           SELECT w
           FROM Wishlist w
           WHERE w.user.userId = :userId
           AND w.product.productId = :productId
           """)
    Optional<Wishlist> findByUserAndProduct(int userId, int productId);

    // Get all wishlist items for a user
    @Query("""
           SELECT w
           FROM Wishlist w
           JOIN FETCH w.product
           LEFT JOIN FETCH ProductImage pi
                ON w.product.productId = pi.product.productId
           WHERE w.user.userId = :userId
           ORDER BY w.createdAt DESC
           """)
    List<Wishlist> findWishlistItemsWithProductDetails(int userId);

    // Count wishlist items
    @Query("""
           SELECT COUNT(w)
           FROM Wishlist w
           WHERE w.user.userId = :userId
           """)
    int countWishlistItems(int userId);

    // Delete one wishlist item
    @Modifying
    @Transactional
    @Query("""
           DELETE
           FROM Wishlist w
           WHERE w.user.userId = :userId
           AND w.product.productId = :productId
           """)
    void deleteWishlistItem(int userId, int productId);

}
