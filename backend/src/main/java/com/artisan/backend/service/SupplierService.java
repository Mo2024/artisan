package com.artisan.backend.service;

import com.artisan.backend.exceptions.UnhandledRejection;
import com.artisan.backend.model.Supplier;
import com.artisan.backend.repository.SupplierRepository;
import com.artisan.backend.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierService {
    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private UserService userService;

    public List<Supplier> getSuppliers(){
        return supplierRepository.findByIsArchived(false);
    }

    @Transactional
    public List<Supplier> createSupplier(Supplier supplier) {

        if (supplier.getName() == null || supplier.getName().trim().isEmpty()) {
            throw new UnhandledRejection("Supplier name must not be empty");
        }

        if (supplier.getDescription() == null || supplier.getDescription().trim().isEmpty()) {
            throw new UnhandledRejection("Supplier description must not be empty");
        }

        if (supplierRepository.existsByName(supplier.getName())) {
            throw new UnhandledRejection("Supplier already exists");
        }

        supplier.setArchived(false);
        supplierRepository.save(supplier);

        return supplierRepository.findByIsArchived(false);
    }

    @Transactional
    public List<Supplier> deleteSupplier(Integer supplierId) {
        if (supplierId == null) {
            throw new UnhandledRejection("Supplier Id name must not be empty");
        }

        boolean supplierExists = supplierRepository.existsById(supplierId);
        if(!supplierExists){
            throw new UnhandledRejection("Supplier does not exist!");
        }

        supplierRepository.archiveSupplierById(supplierId);

        return supplierRepository.findByIsArchived(false);
    }

    @Transactional
    public List<Supplier> editSupplier(Supplier edited_supplier) {

        if (edited_supplier.getName() == null || edited_supplier.getName().trim().isEmpty()) {
            throw new UnhandledRejection("Supplier name must not be empty");
        }

        if (edited_supplier.getDescription() == null || edited_supplier.getDescription().trim().isEmpty()) {
            throw new UnhandledRejection("Supplier description must not be empty");
        }

        if (supplierRepository.existsByNameAndNotId(edited_supplier.getName(), edited_supplier.getId())) {
            throw new UnhandledRejection("Supplier already exists");
        }

        Supplier supplier = supplierRepository.findById(edited_supplier.getId())
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        supplier.setName(edited_supplier.getName());
        supplier.setDescription(edited_supplier.getDescription());
        supplierRepository.save(supplier);

        return supplierRepository.findByIsArchived(false);
    }


}
