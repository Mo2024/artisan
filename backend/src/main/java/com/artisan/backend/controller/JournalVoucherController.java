package com.artisan.backend.controller;

import com.artisan.backend.DTO.ErrorResponse;
import com.artisan.backend.DTO.JournalVoucherDTO;
import com.artisan.backend.exceptions.UnhandledRejection;
import com.artisan.backend.model.JournalVoucher;
import com.artisan.backend.service.JournalVoucherService;
import com.artisan.backend.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/journal_voucher")
public class JournalVoucherController {

    @Autowired
    private JournalVoucherService journalVoucherService;

    @Autowired
    private UserService userService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @GetMapping("/{siteId}")
    public ResponseEntity<?> getJournalVouchers(@PathVariable Integer siteId, Pageable pageable, HttpSession session) {
        try{
            Integer userId = userService.getUserIdFromSession(session);
            Page<JournalVoucher> journalVoucherPage = journalVoucherService.getJournalVouchers(pageable, siteId, userId);
            return ResponseEntity.ok().body(journalVoucherPage);
        } catch (UnhandledRejection e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> createJournalVoucher(@RequestBody JournalVoucherDTO jvRequest, HttpSession session){
        try{
            Integer userId = userService.getUserIdFromSession(session);
            Page<JournalVoucher> journalVoucherPage = journalVoucherService.createJournalVouchers(jvRequest, userId);
            messagingTemplate.convertAndSend("/topic/journal-vouchers", journalVoucherPage);
            return ResponseEntity.ok("Success");
        } catch (UnhandledRejection e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @PutMapping("/")
    public ResponseEntity<?> editJournalVoucher(@RequestBody JournalVoucherDTO jvRequest, HttpSession session){
        try{
            Integer userId = userService.getUserIdFromSession(session);
            Page<JournalVoucher> journalVoucherPage = journalVoucherService.editJournalVouchers(jvRequest, userId);
            messagingTemplate.convertAndSend("/topic/journal-vouchers", journalVoucherPage);
            return ResponseEntity.ok("Success");
        } catch (UnhandledRejection e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }
}

