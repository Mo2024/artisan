package com.artisan.backend.controller;

import com.artisan.backend.DTO.ErrorResponse;
import com.artisan.backend.exceptions.UnhandledRejection;
import com.artisan.backend.model.Creditor;
import com.artisan.backend.service.CreditorService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/creditors")
public class CreditorController {

    @Autowired
    private CreditorService creditorService;

    @GetMapping("/")
    public ResponseEntity<?> getSuppliers() {
        try{
            List<Creditor> creditors = creditorService.getSuppliers();
            return ResponseEntity.ok().body(creditors);
        } catch (UnhandledRejection e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> createSupplier(@RequestBody Creditor creditor){
        try{
            List<Creditor> suppliers = creditorService.createSupplier(creditor);
            return ResponseEntity.ok().body(suppliers);
        } catch (UnhandledRejection e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/{creditorId}")
    public ResponseEntity<?> deleteSite(@PathVariable Integer creditorId, HttpSession session){
        try{
            List<Creditor> suppliers = creditorService.deleteCreditor(creditorId);
            return ResponseEntity.ok().body(suppliers);
        } catch (UnhandledRejection e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @PutMapping("/")
    public ResponseEntity<?> editSupplier(@RequestBody Creditor creditor){
        try{
            List<Creditor> creditors = creditorService.editCreditor(creditor);
            return ResponseEntity.ok().body(creditors);
        } catch (UnhandledRejection e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }
}
