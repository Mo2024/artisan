package com.artisan.backend.repository;

import com.artisan.backend.model.AccountMaster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountMasterRepository extends JpaRepository<AccountMaster, Integer> {

    public List<AccountMaster> findAllByOrderByIdAsc();
}
