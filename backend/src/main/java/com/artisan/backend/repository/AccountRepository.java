package com.artisan.backend.repository;

import com.artisan.backend.model.Account;
import com.artisan.backend.model.Cash;
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
    Optional<Account> findByIdAndUserId(Integer id, Integer userId);

    @Modifying
    @Transactional
    @Query("UPDATE Account a SET a.balance = a.balance - :cost WHERE a.id = :id AND a.user = :user")
    void deductAccountBalance(@Param("cost") BigDecimal cost, @Param("id") Integer id, @Param("user") User user);

    @Modifying
    @Transactional
    @Query("UPDATE Account a SET a.balance = a.balance + :cost WHERE a.id = :id AND a.user = :user")
    void addAccountBalance(@Param("cost") BigDecimal cost, @Param("id") Integer id, @Param("user") User user);

    @Query(nativeQuery = true, value =
            "SELECT * FROM (" +
                    "    SELECT c.id, c.cost, c.date, a.name AS accountName, s.name AS siteName, 'transaction' AS type FROM cash c INNER JOIN accounts a ON c.account_id = a.id INNER JOIN sites s ON c.site_id = s.id WHERE c.account_id = :accountId AND c.user_id = :userId " +
                    "    UNION " +
                    "    SELECT d.id, d.cost, d.date, a.name AS accountName, NULL AS siteName,'transaction' AS type FROM deposits d INNER JOIN accounts a ON d.account_id = a.id WHERE d.account_id = :accountId AND d.user_id = :userId " +
                    ") AS combined_results " +
                    "ORDER BY date DESC")
    List<Object> findByAccountIdAndUserId(@Param("accountId") Integer accountId, @Param("userId") Integer userId);


}
