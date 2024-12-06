package com.artisan.backend.controller;

import com.artisan.backend.DTO.CashRequest;
import com.artisan.backend.DTO.CreditRequest;
import com.artisan.backend.DTO.ErrorResponse;
import com.artisan.backend.exceptions.UnhandledRejection;
import com.artisan.backend.model.Credit;
import com.artisan.backend.service.CreditService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/credit")
public class CreditController {

    @Autowired
    private CreditService creditService;

    @PostMapping("/")
    public ResponseEntity<?> createCredit(@RequestBody CreditRequest new_credit, HttpSession session){
        try{
            List<Credit> credit = creditService.createCredit(new_credit, session);
            return ResponseEntity.ok().body(credit);
        } catch (UnhandledRejection e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @PutMapping("/payCreditor")
    public ResponseEntity<?> payCreditor(@RequestBody CashRequest cashRequest, HttpSession session){
        try{
            List<Credit> credit = creditService.payCreditor(cashRequest, session);
            return ResponseEntity.ok().body(credit);
        } catch (UnhandledRejection e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }
//    @DeleteMapping("/{creditId}")
//    public ResponseEntity<?> deleteCredit(@PathVariable Integer creditId, HttpSession session){
//        try{
//            List<Credit> credit = creditService.deleteCredit(creditId, session);
//            return ResponseEntity.ok().body(credit);
//        } catch (UnhandledRejection e) {
//            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new ErrorResponse(e.getMessage()));
//        }
//    }
//
//    @PutMapping("/")
//    public ResponseEntity<?> editCredit(@RequestBody CreditRequest new_credit, HttpSession session){
//        try{
//            List<Credit> credit = creditService.editCredit(new_credit, session);
//            return ResponseEntity.ok().body(credit);
//        } catch (UnhandledRejection e) {
//            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new ErrorResponse(e.getMessage()));
//        }
//    }

    @GetMapping("/")
    public ResponseEntity<?> getCredit(HttpSession session){
        try{
            List<Credit> credit = creditService.getCredit(session);
            return ResponseEntity.ok().body(credit);
        } catch (UnhandledRejection e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }
}
