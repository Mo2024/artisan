package com.artisan.backend.repository;

import com.artisan.backend.model.Cash;
import com.artisan.backend.model.Site;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CashRepository extends JpaRepository<Cash, Integer> {
    List<Cash> findBySiteIdAndUserId(Integer siteId, Integer userId);
}
