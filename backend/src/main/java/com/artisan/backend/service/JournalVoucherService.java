package com.artisan.backend.service;

import com.artisan.backend.model.AccountMaster;
import com.artisan.backend.model.JournalVoucher;
import com.artisan.backend.repository.JournalVoucherRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class JournalVoucherService {

    @Autowired
    private JournalVoucherRepository journalVoucherRepository;

    @Autowired
    private UserService userService;

    public Page<JournalVoucher> getJournalVouchers(Pageable pageable, Integer siteId, HttpSession session){
        Integer userId = userService.getUserIdFromSession(session);

        return journalVoucherRepository
                .findAllBySiteIdAndUserIdOrderByDateRecordedDesc(siteId, pageable, userId);
    }
}
