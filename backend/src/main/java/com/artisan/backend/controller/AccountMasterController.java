package com.artisan.backend.controller;

import com.artisan.backend.DTO.AccountRequest;
import com.artisan.backend.exceptions.UnhandledRejection;
import com.artisan.backend.model.Account;
import com.artisan.backend.model.AccountMaster;
import com.artisan.backend.service.AccountMasterService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.artisan.backend.DTO.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/account_master")
public class AccountMasterController {

    @Autowired
    private AccountMasterService accountMasterService;

    @GetMapping("/")
    public ResponseEntity<?> getAccountMasters() {
        try{
            List<AccountMaster> accountMasters = accountMasterService.getAccountMasters();
            return ResponseEntity.ok().body(accountMasters);
        } catch (UnhandledRejection e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()) {
            });
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/create_account_master")
    public ResponseEntity<?> createAccountMaster(@RequestBody AccountMaster accountMaster){
        try{
            List<AccountMaster> accountMasterList = accountMasterService.createAccountMaster(accountMaster);
            return ResponseEntity.ok().body(accountMasterList);
        } catch (UnhandledRejection e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @PutMapping("/edit_account_master")
    public ResponseEntity<?> editAccountMaster(@RequestBody AccountMaster accountMaster){
        try{
            List<AccountMaster> accountMasterList = accountMasterService.editAccountMaster(accountMaster);
            return ResponseEntity.ok().body(accountMasterList);
        } catch (UnhandledRejection e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }
}
