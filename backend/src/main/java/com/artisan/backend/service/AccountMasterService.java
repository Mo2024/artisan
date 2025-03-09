package com.artisan.backend.service;

import com.artisan.backend.exceptions.UnhandledRejection;
import com.artisan.backend.model.AccountMaster;
import com.artisan.backend.repository.AccountMasterRepository;
import com.artisan.backend.utility.Functions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountMasterService {

    @Autowired
    private AccountMasterRepository accountMasterRepository;

    @Autowired
    private Functions functions;

    public List<AccountMaster> getAccountMasters(){
        return accountMasterRepository.findAllByOrderByIdAsc();
    }
    public List<AccountMaster> createAccountMaster(AccountMaster accountMaster){
        functions.validateNotNull(accountMaster.getName(), "Account Master Name must not be empty");

        accountMasterRepository.save(accountMaster);

        return getAccountMasters();
    }

    public List<AccountMaster> editAccountMaster(AccountMaster accountMaster){
        functions.validateNotNull(accountMaster.getName(), "Account Master Name must not be empty");
        functions.validateNotNull(accountMaster.getId(), "Account Master ID must not be empty");

        if (!accountMasterRepository.existsById(accountMaster.getId())) {
            throw new UnhandledRejection("Account Master not found with ID: " + accountMaster.getId());
        }

        accountMasterRepository.save(accountMaster);
        return getAccountMasters();
    }
}
