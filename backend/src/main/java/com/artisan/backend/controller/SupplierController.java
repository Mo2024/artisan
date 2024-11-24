package com.artisan.backend.controller;

import com.artisan.backend.DTO.ErrorResponse;
import com.artisan.backend.exceptions.UnhandledRejection;
import com.artisan.backend.model.Supplier;
import com.artisan.backend.service.SupplierService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    @GetMapping("/")
    public ResponseEntity<?> getSuppliers() {
        try{
            List<Supplier> suppliers = supplierService.getSuppliers();
            return ResponseEntity.ok().body(suppliers);
        } catch (UnhandledRejection e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> createSupplier(@RequestBody Supplier supplier){
        try{
            List<Supplier> suppliers = supplierService.createSupplier(supplier);
            return ResponseEntity.ok().body(suppliers);
        } catch (UnhandledRejection e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/{supplierId}")
    public ResponseEntity<?> deleteSite(@PathVariable Integer supplierId, HttpSession session){
        try{
            List<Supplier> suppliers = supplierService.deleteSupplier(supplierId);
            return ResponseEntity.ok().body(suppliers);
        } catch (UnhandledRejection e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @PutMapping("/")
    public ResponseEntity<?> editSupplier(@RequestBody Supplier supplier){
        try{
            List<Supplier> suppliers = supplierService.editSupplier(supplier);
            return ResponseEntity.ok().body(suppliers);
        } catch (UnhandledRejection e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }
}
