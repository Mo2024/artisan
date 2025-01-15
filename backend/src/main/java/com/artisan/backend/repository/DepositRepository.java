package com.artisan.backend.repository;

import com.artisan.backend.model.Cash;
import com.artisan.backend.model.Deposit;
import com.artisan.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepositRepository extends JpaRepository<Deposit, Integer> {
    List<Deposit> findByUserIdAndAccountIdOrderByDateDesc(Integer userId, Integer accountId);
}
