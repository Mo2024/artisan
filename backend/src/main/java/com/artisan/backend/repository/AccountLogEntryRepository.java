package com.artisan.backend.repository;

import com.artisan.backend.model.Account;
import com.artisan.backend.model.AccountLogEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountLogEntryRepository extends JpaRepository<AccountLogEntry, Integer> {

}
