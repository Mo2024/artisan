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
import java.util.Date;
import java.util.List;
import java.util.Objects;

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
        functions.validateNotNull(new_cash.getCost(), "Cost must not be empty");
        functions.validateNotNull(new_cash.getDescription(), "Description must not be empty");
        functions.validateNotNull(new_cash.getSiteId(), "Site must not be empty");

//        if()

        Site site = siteRepository.findByIdAndUserId(new_cash.getSiteId(), userId)
                .orElseThrow(() -> new RuntimeException("site not found"));


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
        cash.setUser(user);
        if (new_cash.getIsCredit()){
            cash.setIsCredit(true);
            cash.setCredit(new_cash.getCredit());
        } else {
            cash.setIsCredit(false);
            cash.setCredit(null);
        }


        if (new_cash.getPaymentMethod().equals("bank account")){
            functions.validateNotNull(new_cash.getAccountId(), "Account must not be empty");
            Account account = accountRepository.findByIdAndUserId(new_cash.getAccountId(), userId)
                    .orElseThrow(() -> new RuntimeException("Account not found"));
            if (account.getBalance().compareTo(new_cash.getCost()) < 0) {
                throw new IllegalArgumentException("Insufficient balance for this transaction");
            }
            cash.setAccount(account);
            accountRepository.deductAccountBalance(new_cash.getCost(), new_cash.getAccountId(), user);
        }
        cashRepository.save(cash);
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
        functions.validateNotNull(new_cash.getCost(), "Cost must not be empty");
        functions.validateNotNull(new_cash.getDescription(), "Description must not be empty");
        functions.validateNotNull(new_cash.getSiteId(), "Site must not be empty");

        Site site = siteRepository.findByIdAndUserId(new_cash.getSiteId(), userId)
                .orElseThrow(() -> new RuntimeException("site not found"));

        if (new_cash.getDate().after(new Date())) {
            throw new IllegalArgumentException("Date must not be in the future");
        }

        if (new_cash.getCost().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Cost must be greater than zero");
        }



        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cash cash = cashRepository.findByIdAndUserId(new_cash.getId(), userId)
                .orElseThrow(() -> new RuntimeException("cash not found"));

        if(!cash.getPaymentMethod().equals("bank account") && new_cash.getPaymentMethod().equals("bank account")) {
            functions.validateNotNull(new_cash.getAccountId(), "Account must not be empty");
            Account account = accountRepository.findByIdAndUserId(new_cash.getAccountId(), userId)
                    .orElseThrow(() -> new RuntimeException("Account not found"));
            if (account.getBalance().compareTo(new_cash.getCost()) < 0) {
                throw new IllegalArgumentException("Insufficient balance for this transaction");
            }
            accountRepository.deductAccountBalance(new_cash.getCost(), new_cash.getAccountId(), user);
            cash.setAccount(account);
        }

        else if (cash.getPaymentMethod().equals("bank account") && !new_cash.getPaymentMethod().equals("bank account")) {
            accountRepository.addAccountBalance(cash.getCost(), cash.getAccount().getId(), user);
            cash.setAccount(null);
        }

        else if (cash.getPaymentMethod().equals("bank account") &&
                new_cash.getPaymentMethod().equals("bank account") &&
                Objects.equals(cash.getAccount().getId(), new_cash.getAccountId()) &&
                !Objects.equals(cash.getCost(), new_cash.getCost())) {

            functions.validateNotNull(new_cash.getAccountId(), "Account must not be empty");

            BigDecimal balanceChange = new_cash.getCost().subtract(cash.getCost()); // Calculate the balance change
            BigDecimal updatedBalance = cash.getAccount().getBalance().subtract(balanceChange); // Simulate the new balance

            if (updatedBalance.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Insufficient balance in the account for this transaction");
            }

            accountRepository.deductAccountBalance(balanceChange, new_cash.getAccountId(), user);
        }
        else if (cash.getPaymentMethod().equals("bank account") &&
                new_cash.getPaymentMethod().equals("bank account") &&
                !Objects.equals(cash.getAccount().getId(), new_cash.getAccountId())) {

            functions.validateNotNull(new_cash.getAccountId(), "Account must not be empty");

            Account oldAccount = accountRepository.findByIdAndUserId(cash.getAccount().getId(), userId)
                    .orElseThrow(() -> new RuntimeException("Account not found"));
            BigDecimal oldAccountBalance = oldAccount.getBalance();

            Account newAccount = accountRepository.findByIdAndUserId(new_cash.getAccountId(), userId)
                    .orElseThrow(() -> new RuntimeException("Account not found"));
            BigDecimal newAccountBalance = newAccount.getBalance();

            if (oldAccountBalance.add(cash.getCost()).compareTo(BigDecimal.ZERO) < 0 ||
                    newAccountBalance.subtract(new_cash.getCost()).compareTo(BigDecimal.ZERO) < 0) {
                throw new UnhandledRejection("Insufficient balance in the old or new account for this transaction");
            }
            System.out.println(new_cash.getCost());
            System.out.println(cash.getCost());
            accountRepository.deductAccountBalance(new_cash.getCost(), new_cash.getAccountId(), user);
            accountRepository.addAccountBalance(cash.getCost(), cash.getAccount().getId(), user);
            cash.setAccount(newAccount);
        }


        cash.setDate(new_cash.getDate());
        cash.setPaidBy(new_cash.getPaidBy());
        cash.setPaymentMethod(new_cash.getPaymentMethod());
        cash.setCost(new_cash.getCost());
        cash.setDescription(new_cash.getDescription());
        cash.setDateEdited(new Date());
        cash.setSite(site);

        cashRepository.save(cash);
        return cashRepository.findByUserId(userId);
    }

    @Transactional
    public List<Cash> deleteCash(Integer id, HttpSession session){
        Integer userId = userService.getUserIdFromSession(session);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cash cash = cashRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("cash not found"));

        if (id == null) {
            throw new UnhandledRejection("Cash Id name must not be empty");
        }

        boolean cashExists = cashRepository.existsByIdAndUserId(id, userId);
        if(!cashExists){
            throw new UnhandledRejection("Transaction Not found.!");
        }

        accountRepository.addAccountBalance(cash.getCost() ,id, user);
        cashRepository.deleteById(id);
        return cashRepository.findByUserId(userId);
    }
}
