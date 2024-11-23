package com.artisan.backend.repository;

import com.artisan.backend.model.Site;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SiteRepository extends JpaRepository<Site, Integer> {
    List<Site> findByUserId(Integer userId);
    boolean existsByNameAndUserId(String name, Integer userId);
    boolean existsByIdAndUserId(Integer id, Integer userId);

}
