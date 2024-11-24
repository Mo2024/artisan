package com.artisan.backend.repository;

import com.artisan.backend.model.Account;
import com.artisan.backend.model.Site;
import com.artisan.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    List<Account> findByUserId(Integer userId);
    boolean existsByNameAndUserId(String name, Integer userId);
}
