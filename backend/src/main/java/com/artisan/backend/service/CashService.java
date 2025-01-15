package com.artisan.backend.service;

import com.artisan.backend.DTO.CashRequest;
import com.artisan.backend.DTO.GetTransactionsDTO;
import com.artisan.backend.exceptions.UnhandledRejection;
import com.artisan.backend.model.Account;
import com.artisan.backend.model.Cash;
import com.artisan.backend.model.Site;
import com.artisan.backend.model.User;
import com.artisan.backend.repository.*;
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
    private DepositRepository depositRepository;

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

    @Autowired
    private AccountLogEntryService aleService;

    @Transactional
    public List<Cash> createCash(CashRequest new_cash, HttpSession session) {
        Integer userId = userService.getUserIdFromSession(session);

        functions.validateNotNull(new_cash.getDate(), "Date must not be empty");
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
        cash.setCost(new_cash.getCost());
        cash.setDescription(new_cash.getDescription());
        cash.setDateRecorded(new Date());
        cash.setDateEdited(null);
        cash.setSite(site);
        cash.setUser(user);
        if (new_cash.getIsCredit() != null && new_cash.getIsCredit()){
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
        if (new_cash.getIsCredit() != null && new_cash.getIsCredit()){
            Account account = accountRepository.findByIdAndUserId(new_cash.getAccountId(), userId)
                    .orElseThrow(() -> new RuntimeException("Account not found"));
            aleService.insertLog(
                    cash.getId(),
                    account.getId(),
                    userId,
                    site.getId(),
                    new BigDecimal(String.valueOf(account.getBalance())),
                    new BigDecimal(String.valueOf(account.getBalance().subtract(new_cash.getCost()))),
                    new BigDecimal(String.valueOf(new_cash.getCost())),
            "CREDITOR_PAYMENT_TRANSACTION"
            );
        } else {
            if(new_cash.getPaymentMethod().equals("bank account")){
                Account account = accountRepository.findByIdAndUserId(new_cash.getAccountId(), userId)
                        .orElseThrow(() -> new RuntimeException("Account not found"));
                aleService.insertLog(
                        cash.getId(),
                        account.getId(),
                        userId,
                        site.getId(),
                        new BigDecimal(String.valueOf(account.getBalance())),
                        new BigDecimal(String.valueOf(account.getBalance().subtract(new_cash.getCost()))),
                        new BigDecimal(String.valueOf(new_cash.getCost())),
                        "NORMAL_PAYMENT_TRANSACTION"
                );
            }
        }
        return cashRepository.findByUserId(userId);
    }

    public List<Cash> getCash(HttpSession session){
        Integer userId = userService.getUserIdFromSession(session);
        return cashRepository.findByUserId(userId);
    }

    public GetTransactionsDTO getCashByAccountId(Integer accountId, HttpSession session){
        Integer userId = userService.getUserIdFromSession(session);
        GetTransactionsDTO transactionsByAccount = new GetTransactionsDTO();

        Account account = accountRepository.findByIdAndUserId(accountId, userId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        transactionsByAccount.setTransactions(cashRepository.findByUserIdAndAccountIdOrderByDateDesc(userId, accountId));
        transactionsByAccount.setDeposits(depositRepository.findByUserIdAndAccountIdOrderByDateDesc(userId, accountId));
        transactionsByAccount.setAccount(account);

        return  transactionsByAccount;
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

        if(cash.getIsCredit()){
            throw new UnhandledRejection("Cannot delete a paid transaction");
        }


        aleService.insertLog(
                cash.getId(),
                cash.getAccount().getId(),
                userId,
                cash.getSite().getId(),
                new BigDecimal(String.valueOf(cash.getAccount().getBalance())),
                new BigDecimal(String.valueOf(cash.getAccount().getBalance().add(cash.getCost()))),
                new BigDecimal(String.valueOf(cash.getCost())),
                "DELETE_NORMAL_PAYMENT_TRANSACTION"
        );



        accountRepository.addAccountBalance(cash.getCost() ,cash.getAccount().getId(), user);
        cashRepository.deleteById(id);
        return cashRepository.findByUserId(userId);
    }
}
