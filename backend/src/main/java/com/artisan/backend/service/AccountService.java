package com.artisan.backend.service;

import com.artisan.backend.exceptions.UnhandledRejection;
import com.artisan.backend.model.Account;
import com.artisan.backend.model.Site;
import com.artisan.backend.model.User;
import com.artisan.backend.repository.AccountRepository;
import com.artisan.backend.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;


    public List<Account> getAccounts(HttpSession session){
        Integer userId = userService.getUserIdFromSession(session);
        return accountRepository.findByUserId(userId);
    }

    @Transactional
    public List<Account> createAccount(Account account, HttpSession session) {
        Integer userId = userService.getUserIdFromSession(session);

        if (account.getName() == null || account.getName().trim().isEmpty()) {
            throw new UnhandledRejection("Account name must not be empty");
        }

        if (account.getBalance() == null) {
            throw new UnhandledRejection("Balance must not be null");
        }


        if (accountRepository.existsByNameAndUserId(account.getName(), userId)) {
            throw new UnhandledRejection("Account already exists");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        account.setUser(user);
        accountRepository.save(account);

        return accountRepository.findByUserId(userId);
    }


}
