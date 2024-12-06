package com.artisan.backend.repository;

import com.artisan.backend.model.Creditor;
import com.artisan.backend.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CreditorRepository extends JpaRepository<Creditor, Integer> {
    List<Creditor> findByIsArchived(boolean isArchived);
    boolean existsByName(String name);

    @Modifying
    @Transactional
    @Query("UPDATE Creditor s SET s.isArchived = true WHERE s.id = :id")
    void archiveSupplierById(Integer id);

    @Query("SELECT COUNT(s) > 0 FROM Creditor s WHERE s.name = :name AND s.id <> :id")
    boolean existsByNameAndNotId(@Param("name") String name, @Param("id") Integer id);

}
