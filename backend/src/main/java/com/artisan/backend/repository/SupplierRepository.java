package com.artisan.backend.repository;

import com.artisan.backend.model.Supplier;
import com.artisan.backend.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SupplierRepository extends JpaRepository<Supplier, Integer> {
    List<Supplier> findByIsArchived(boolean isArchived);
    boolean existsByName(String name);

    @Modifying
    @Transactional
    @Query("UPDATE Supplier s SET s.isArchived = true WHERE s.id = :id")
    void archiveSupplierById(Integer id);

    @Query("SELECT COUNT(s) > 0 FROM Supplier s WHERE s.name = :name AND s.id <> :id")
    boolean existsByNameAndNotId(@Param("name") String name, @Param("id") Integer id);

}
