package com.kodnest.SalesSavvy_App.Repositories;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kodnest.SalesSavvy_App.Entities.User;


public interface UserRepository extends JpaRepository<User, Integer> {
	Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
}