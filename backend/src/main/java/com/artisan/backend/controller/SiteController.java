package com.artisan.backend.controller;

import com.artisan.backend.DTO.ErrorResponse;
import com.artisan.backend.DTO.UserId;
import com.artisan.backend.exceptions.UnhandledRejection;
import com.artisan.backend.model.Site;
import com.artisan.backend.service.SiteService;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sites")
public class SiteController {

    @Autowired
    private SiteService siteService;

    // Get all sites by userID
    @GetMapping("/")
    public ResponseEntity<?> getSitesById(HttpSession session) {
        try{
             List<Site> sites = siteService.getSites(session);
            return ResponseEntity.ok().body(sites);
        } catch (UnhandledRejection e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> createSite(@RequestBody Site site, HttpSession session){
        try{
            List<Site> sites = siteService.createSite(site, session);
            return ResponseEntity.ok().body(sites);
        } catch (UnhandledRejection e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/{siteId}")
    public ResponseEntity<?> deleteSite(@PathVariable Integer siteId, HttpSession session){
        try{
            List<Site> sites = siteService.deleteSite(siteId, session);
            return ResponseEntity.ok().body(sites);
        } catch (UnhandledRejection e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @PutMapping("/")
    public ResponseEntity<?> editSite(@RequestBody Site site, HttpSession session){
        try{
            List<Site> sites = siteService.editSite(site, session);
            return ResponseEntity.ok().body(sites);
        } catch (UnhandledRejection e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

}
