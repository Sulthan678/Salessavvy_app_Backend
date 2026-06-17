package com.kodnest.SalesSavvy_App.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kodnest.SalesSavvy_App.Entities.Category;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
	Optional<Category> findByCategoryNameIgnoreCase(String categoryName);
}
