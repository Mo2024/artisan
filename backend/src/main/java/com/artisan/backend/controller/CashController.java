package com.artisan.backend.controller;

import com.artisan.backend.DTO.AccountRequest;
import com.artisan.backend.DTO.CashRequest;
import com.artisan.backend.DTO.ErrorResponse;
import com.artisan.backend.DTO.GetTransactionsDTO;
import com.artisan.backend.exceptions.UnhandledRejection;
import com.artisan.backend.model.Cash;
import com.artisan.backend.service.CashService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cash")
public class CashController {

    @Autowired
    private CashService cashService;

    @PostMapping("/")
    public ResponseEntity<?> createCash(@RequestBody CashRequest new_cash, HttpSession session){
        try{
            List<Cash> cash = cashService.createCash(new_cash, session);
            return ResponseEntity.ok().body(cash);
        } catch (UnhandledRejection e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }
    @DeleteMapping("/{cashId}")
    public ResponseEntity<?> deleteSite(@PathVariable Integer cashId, HttpSession session){
        try{
            List<Cash> cash = cashService.deleteCash(cashId, session);
            return ResponseEntity.ok().body(cash);
        } catch (UnhandledRejection e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/")
    public ResponseEntity<?> getCash(HttpSession session){
        try{
            List<Cash> cash = cashService.getCash(session);
            return ResponseEntity.ok().body(cash);
        } catch (UnhandledRejection e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }
    @GetMapping("/getByAccountId/{accountId}")
    public ResponseEntity<?> getCashByAccountId(@PathVariable Integer accountId, HttpSession session){
        try{
            GetTransactionsDTO cash = cashService.getCashByAccountId(accountId, session);
            return ResponseEntity.ok().body(cash);
        } catch (UnhandledRejection e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

}
