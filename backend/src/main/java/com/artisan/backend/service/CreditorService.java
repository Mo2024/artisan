package com.artisan.backend.service;

import com.artisan.backend.exceptions.UnhandledRejection;
import com.artisan.backend.model.Creditor;
import com.artisan.backend.repository.CreditorRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreditorService {
    @Autowired
    private CreditorRepository creditorRepository;

    @Autowired
    private UserService userService;

    public List<Creditor> getSuppliers(){
        return creditorRepository.findByIsArchived(false);
    }

    @Transactional
    public List<Creditor> createSupplier(Creditor creditor) {

        if (creditor.getName() == null || creditor.getName().trim().isEmpty()) {
            throw new UnhandledRejection("Creditor name must not be empty");
        }

        if (creditor.getDescription() == null || creditor.getDescription().trim().isEmpty()) {
            throw new UnhandledRejection("Creditor description must not be empty");
        }

        if (creditorRepository.existsByName(creditor.getName())) {
            throw new UnhandledRejection("Creditor already exists");
        }

        creditor.setArchived(false);
        creditorRepository.save(creditor);

        return creditorRepository.findByIsArchived(false);
    }

    @Transactional
    public List<Creditor> deleteCreditor(Integer supplierId) {
        if (supplierId == null) {
            throw new UnhandledRejection("Creditor Id name must not be empty");
        }

        boolean supplierExists = creditorRepository.existsById(supplierId);
        if(!supplierExists){
            throw new UnhandledRejection("Creditor does not exist!");
        }

        creditorRepository.archiveSupplierById(supplierId);

        return creditorRepository.findByIsArchived(false);
    }

    @Transactional
    public List<Creditor> editCreditor(Creditor edited_creditor) {

        if (edited_creditor.getName() == null || edited_creditor.getName().trim().isEmpty()) {
            throw new UnhandledRejection("Creditor name must not be empty");
        }

        if (edited_creditor.getDescription() == null || edited_creditor.getDescription().trim().isEmpty()) {
            throw new UnhandledRejection("Creditor description must not be empty");
        }

        if (creditorRepository.existsByNameAndNotId(edited_creditor.getName(), edited_creditor.getId())) {
            throw new UnhandledRejection("Creditor already exists");
        }

        Creditor creditor = creditorRepository.findById(edited_creditor.getId())
                .orElseThrow(() -> new RuntimeException("Creditor not found"));

        creditor.setName(edited_creditor.getName());
        creditor.setDescription(edited_creditor.getDescription());
        creditorRepository.save(creditor);

        return creditorRepository.findByIsArchived(false);
    }


}
