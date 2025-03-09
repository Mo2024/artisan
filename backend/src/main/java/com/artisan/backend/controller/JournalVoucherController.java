package com.artisan.backend.controller;

import com.artisan.backend.DTO.ErrorResponse;
import com.artisan.backend.exceptions.UnhandledRejection;
import com.artisan.backend.model.JournalVoucher;
import com.artisan.backend.service.JournalVoucherService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/journal_voucher")
public class JournalVoucherController {

    @Autowired
    private JournalVoucherService journalVoucherService;

    @GetMapping("/{siteId}")
    public ResponseEntity<?> getSitesById(@PathVariable Integer siteId, Pageable pageable, HttpSession session) {
        try{
            Page<JournalVoucher> journalVoucherPage = journalVoucherService.getJournalVouchers(pageable, siteId, session);
            return ResponseEntity.ok().body(journalVoucherPage);
        } catch (UnhandledRejection e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }
}
