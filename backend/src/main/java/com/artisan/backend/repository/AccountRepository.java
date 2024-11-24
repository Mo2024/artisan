package com.artisan.backend.repository;

import com.artisan.backend.model.Account;
import com.artisan.backend.model.Site;
import com.artisan.backend.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    List<Account> findByUserId(Integer userId);
    boolean existsByNameAndUserId(String name, Integer userId);

    @Modifying
    @Transactional
    @Query("UPDATE Account a SET a.balance = a.balance - :cost WHERE a.id = :id AND a.user = :user")
    void deductAccountBalance(@Param("cost") BigDecimal cost, @Param("id") Integer id, @Param("user") User user);
    Optional<Account> findByIdAndUserId(Integer id, Integer userId);

}
