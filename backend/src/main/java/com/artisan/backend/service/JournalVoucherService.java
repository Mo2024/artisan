package com.artisan.backend.service;

import com.artisan.backend.DTO.JournalVoucherDTO;
import com.artisan.backend.model.AccountMaster;
import com.artisan.backend.model.JournalVoucher;
import com.artisan.backend.repository.JournalVoucherRepository;
import com.artisan.backend.utility.Functions;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class JournalVoucherService {

    @Autowired
    private JournalVoucherRepository journalVoucherRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private Functions functions;

    public Page<JournalVoucher> getJournalVouchers(Pageable pageable, Integer siteId, HttpSession session){
        Integer userId = userService.getUserIdFromSession(session);

        return journalVoucherRepository
                .findAllBySiteIdAndUserIdOrderByDateRecordedDesc(siteId, pageable, userId);
    }

    public Page<JournalVoucher> createJournalVouchers(JournalVoucherDTO jvRequest, HttpSession httpSession){
        Pageable pageable = PageRequest.of(jvRequest.getPage(), jvRequest.getSize());

        functions.validateNotNull(jvRequest.getAmount(), "Amount must not be empty");
        functions.isValidBigDecimal(jvRequest.getAmount());
        functions.validateNotNull(jvRequest.getDate(), "Date must not be empty");
        functions.validateNotNull(jvRequest.getDescription(), "Description must not be empty");

        JournalVoucher journalVoucher = JournalVoucher.builder()
                .crMaster(jvRequest.getCrMaster())
                .drMaster(jvRequest.getDrMaster())
                .site(jvRequest.getSite())
                .amount(new BigDecimal(jvRequest.getAmount()))
                .user(jvRequest.getSite().getUser())
                .date(jvRequest.getDate())
                .description(jvRequest.getDescription())
                .dateRecorded(new Date())
                .dateEdited(null)
                .build();

        journalVoucherRepository.save(journalVoucher);

        return getJournalVouchers(pageable, jvRequest.getSite().getId(), httpSession);
    }
}
