package com.artisan.backend.repository;

import com.artisan.backend.model.Cash;
import com.artisan.backend.model.Credit;
import com.artisan.backend.model.Site;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CreditRepository extends JpaRepository<Credit, Integer> {
    List<Credit> findByUserId(Integer userId);
    List<Credit> findByUserIdAndIsPaid(Integer userId, boolean isPaid);
    Optional<Credit> findByIdAndUserId(Integer id, Integer userId);
    boolean existsByIdAndUserId(Integer id, Integer userId);

}
