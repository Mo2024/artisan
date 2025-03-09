package com.artisan.backend.repository;

import com.artisan.backend.model.JournalVoucher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JournalVoucherRepository extends JpaRepository<JournalVoucher, Integer> {
    Page<JournalVoucher> findAllBySiteIdAndUserIdOrderByDateRecordedDesc(Integer siteId, Pageable pageable, Integer userId);
 }
