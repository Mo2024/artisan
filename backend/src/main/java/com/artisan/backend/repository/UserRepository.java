package com.artisan.backend.repository;

import com.artisan.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsByUsername(String username);
    User findByUsername(String username);
}
