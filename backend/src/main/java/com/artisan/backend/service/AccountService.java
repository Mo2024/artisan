package com.artisan.backend.service;

import com.artisan.backend.DTO.AccountRequest;
import com.artisan.backend.DTO.Functions;
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

    @Autowired
    private AccountLogEntryService aleService;


    public List<Account> getAccounts(HttpSession session){
        Integer userId = userService.getUserIdFromSession(session);
        return accountRepository.findByUserId(userId);
    }

    @Transactional
    public List<Account> addBalance(AccountRequest accountRequest, HttpSession session) {
        Integer userId = userService.getUserIdFromSession(session);

        // Fetch account ensuring it belongs to the logged-in user
        Account account = accountRepository.findByIdAndUserId(accountRequest.getAccountId(), userId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (!Functions.isValidBigDecimal(accountRequest.getAddedBalance())) {
            throw new IllegalArgumentException("Invalid balance format: " + accountRequest.getAddedBalance());
        }

        BigDecimal addedBalance = new BigDecimal(accountRequest.getAddedBalance());

        // Validate that the added balance is greater than zero
        if (addedBalance.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Added balance must be greater than zero");
        }

        String oldBalance =  String.valueOf(account.getBalance());

        // Update the account balance
        account.setBalance(account.getBalance().add(addedBalance));

        // Save the updated account
        accountRepository.save(account);


        aleService.insertLog(
                null,
                account.getId(),
                userId,
                null,
                new BigDecimal(oldBalance),
                new BigDecimal(String.valueOf(account.getBalance())),
                new BigDecimal(accountRequest.getAddedBalance()),
                "ADD_BALANCE_TO_ACCOUNT"
        );

        // Return the updated list of accounts for the user
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
        aleService.insertLog(
                null,
                account.getId(),
                userId,
                null,
                new BigDecimal("0"),
                new BigDecimal(String.valueOf(account.getBalance())),
                null,
                "CREATE_ACCOUNT"
        );
        return accountRepository.findByUserId(userId);
    }


}
