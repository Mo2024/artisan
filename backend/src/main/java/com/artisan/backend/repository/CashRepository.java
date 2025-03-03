package com.artisan.backend.repository;

import com.artisan.backend.model.Cash;
import com.artisan.backend.model.Site;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CashRepository extends JpaRepository<Cash, Integer> {
    List<Cash> findByUserId(Integer userId);
    Optional<Cash> findByIdAndUserId(Integer id, Integer userId);
    boolean existsByIdAndUserId(Integer id, Integer userId);
    List<Cash> findByUserIdAndAccountIdOrderByDateDesc(Integer userId, Integer accountId);


}
