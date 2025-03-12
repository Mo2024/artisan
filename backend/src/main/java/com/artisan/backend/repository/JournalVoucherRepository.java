package com.artisan.backend.repository;

import com.artisan.backend.model.JournalVoucher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JournalVoucherRepository extends JpaRepository<JournalVoucher, Integer> {
//    Page<JournalVoucher> findAllBySiteIdAndUserIdOrderByDateRecordedDesc(Integer siteId, Pageable pageable, Integer userId);

    @Query(value = "SELECT * FROM journal_voucher jv " +
            "WHERE jv.site_id = :siteId AND jv.user_id = :userId " +
            "ORDER BY jv.date_recorded DESC " +
            "LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<JournalVoucher> findAllBySiteIdAndUserIdOrderByDateRecordedDesc(
            @Param("siteId") Integer siteId,
            @Param("userId") Integer userId,
            @Param("limit") int limit,
            @Param("offset") int offset
    );
 }
