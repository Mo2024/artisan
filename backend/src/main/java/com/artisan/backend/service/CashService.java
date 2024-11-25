package com.artisan.backend.service;

import com.artisan.backend.DTO.CashRequest;
import com.artisan.backend.exceptions.UnhandledRejection;
import com.artisan.backend.model.Account;
import com.artisan.backend.model.Cash;
import com.artisan.backend.model.Site;
import com.artisan.backend.model.User;
import com.artisan.backend.repository.AccountRepository;
import com.artisan.backend.repository.CashRepository;
import com.artisan.backend.repository.SiteRepository;
import com.artisan.backend.repository.UserRepository;
import com.artisan.backend.utility.Functions;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
public class CashService {
    @Autowired
    private CashRepository cashRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private Functions functions;

    @Autowired
    private SiteRepository siteRepository;

    @Autowired
    private AccountRepository accountRepository;


    @Transactional
    public List<Cash> createCash(CashRequest new_cash, HttpSession session) {
        Integer userId = userService.getUserIdFromSession(session);

        functions.validateNotNull(new_cash.getDate(), "Date must not be empty");
        functions.validateNotNull(new_cash.getPaidBy(), "Payer must not be empty");
        functions.validateNotNull(new_cash.getPaymentMethod(), "Payment method must not be empty");
        functions.validateNotNull(new_cash.getAccountId(), "Account must not be empty");
        functions.validateNotNull(new_cash.getCost(), "Cost must not be empty");
        functions.validateNotNull(new_cash.getDescription(), "Description must not be empty");
        functions.validateNotNull(new_cash.getSiteId(), "Site must not be empty");

        Site site = siteRepository.findByIdAndUserId(new_cash.getSiteId(), userId)
                .orElseThrow(() -> new RuntimeException("site not found"));

        Account account = accountRepository.findByIdAndUserId(new_cash.getAccountId(), userId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (new_cash.getDate().after(new Date())) {
            throw new IllegalArgumentException("Date must not be in the future");
        }

        if (new_cash.getCost().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Cost must be greater than zero");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cash cash = new Cash();
        cash.setDate(new_cash.getDate());
        cash.setPaidBy(new_cash.getPaidBy());
        cash.setPaymentMethod(new_cash.getPaymentMethod());
        cash.setCost(new_cash.getCost());
        cash.setDescription(new_cash.getDescription());
        cash.setDateRecorded(new Date());
        cash.setDateEdited(null);
        cash.setSite(site);
        cash.setAccount(account);
        cash.setUser(user);

        cashRepository.save(cash);

        if (new_cash.getPaymentMethod().equals("bank account")){
            if (account.getBalance().compareTo(new_cash.getCost()) < 0) {
                throw new IllegalArgumentException("Insufficient balance for this transaction");
            }
            accountRepository.deductAccountBalance(new_cash.getCost(), new_cash.getAccountId(), user);
        }

        return cashRepository.findByUserId(userId);
    }

    public List<Cash> getCash(HttpSession session){
        Integer userId = userService.getUserIdFromSession(session);
        return cashRepository.findByUserId(userId);
    }

    @Transactional
    public List<Cash> editCash(CashRequest new_cash, HttpSession session) {
        Integer userId = userService.getUserIdFromSession(session);

        functions.validateNotNull(new_cash.getDate(), "Date must not be empty");
        functions.validateNotNull(new_cash.getPaidBy(), "Payer must not be empty");
        functions.validateNotNull(new_cash.getPaymentMethod(), "Payment method must not be empty");
        functions.validateNotNull(new_cash.getAccountId(), "Account must not be empty");
        functions.validateNotNull(new_cash.getCost(), "Cost must not be empty");
        functions.validateNotNull(new_cash.getDescription(), "Description must not be empty");
        functions.validateNotNull(new_cash.getSiteId(), "Site must not be empty");

        Site site = siteRepository.findByIdAndUserId(new_cash.getSiteId(), userId)
                .orElseThrow(() -> new RuntimeException("site not found"));

        Account account = accountRepository.findByIdAndUserId(new_cash.getAccountId(), userId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (new_cash.getDate().after(new Date())) {
            throw new IllegalArgumentException("Date must not be in the future");
        }

        if (new_cash.getCost().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Cost must be greater than zero");
        }

        if (account.getBalance().compareTo(new_cash.getCost()) < 0) {
            throw new IllegalArgumentException("Insufficient balance for this transaction");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cash cash = cashRepository.findByIdAndUserId(new_cash.getId(), userId)
                .orElseThrow(() -> new RuntimeException("cash not found"));

        if(cash.getPaymentMethod().equals("Cash") && new_cash.getPaymentMethod().equals("bank account")){
            if(account.getBalance() - new_cash.getCost() < 0){

            }
        }

        cash.setDate(new_cash.getDate());
        cash.setPaidBy(new_cash.getPaidBy());
        cash.setPaymentMethod(new_cash.getPaymentMethod());
        cash.setCost(new_cash.getCost());
        cash.setDescription(new_cash.getDescription());
        cash.setDateEdited(new Date());
        cash.setSite(site);
        cash.setAccount(account);

        cashRepository.save(cash);

        accountRepository.deductAccountBalance(new_cash.getCost(), new_cash.getAccountId(), user);

        return cashRepository.findByUserId(userId);

    }

}
