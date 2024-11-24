package com.artisan.backend.repository;

import com.artisan.backend.model.Site;
import com.artisan.backend.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SiteRepository extends JpaRepository<Site, Integer> {
    List<Site> findByUserIdAndIsArchived(Integer userId, boolean isArchived);
    boolean existsByNameAndUserId(String name, Integer userId);
    boolean existsByIdAndUserId(Integer id, Integer userId);
    Optional<Site> findByIdAndUserId(Integer id, Integer userId);
    @Query("SELECT COUNT(s) > 0 FROM Site s WHERE s.name = :name AND s.user = :user AND s.id <> :id")
    boolean existsByNameAndUserIdAndNotId(@Param("name") String name, @Param("user") User user, @Param("id") Integer siteId);

    @Modifying
    @Transactional
    @Query("UPDATE Site s SET s.isArchived = true WHERE s.id = :id")
    void archiveSiteById(@Param("id") Integer id);

}
