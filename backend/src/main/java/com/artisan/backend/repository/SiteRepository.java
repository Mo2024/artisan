package com.artisan.backend.repository;

import com.artisan.backend.model.Site;
import com.artisan.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SiteRepository extends JpaRepository<Site, Integer> {
    List<Site> findByUserId(Integer userId);
    boolean existsByNameAndUserId(String name, Integer userId);
    boolean existsByIdAndUserId(Integer id, Integer userId);

    @Query("SELECT COUNT(s) > 0 FROM Site s WHERE s.name = :name AND s.user = :user AND s.id <> :id")
    boolean existsByNameAndUserIdAndNotId(@Param("name") String name, @Param("user") User user, @Param("id") Integer siteId);
//    boolean existsByN/ameAndUserIdAndNotId(String name, Integer userId, Integer siteId);

}
