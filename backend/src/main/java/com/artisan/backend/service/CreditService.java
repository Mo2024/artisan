package com.artisan.backend.service;

import com.artisan.backend.DTO.CashRequest;
import com.artisan.backend.DTO.CreditRequest;
import com.artisan.backend.model.*;
import com.artisan.backend.repository.*;
import com.artisan.backend.utility.Functions;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class CreditService {

    @Autowired
    private CreditRepository creditRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SiteRepository siteRepository;
    @Autowired
    private UserService userService;

    @Autowired
    private Functions functions;

    @Autowired
    private CashService cashService;

    @Autowired
    private CreditorRepository creditorRepository;

    public List<Credit> getCredit(HttpSession session){
        Integer userId = userService.getUserIdFromSession(session);
        return creditRepository.findByUserIdAndIsPaid(userId, false);
    }

    @Transactional
    public List<Credit> createCredit(CreditRequest new_credit, HttpSession session) {
        Integer userId = userService.getUserIdFromSession(session);

        functions.validateNotNull(new_credit.getDate(), "Date must not be empty");
        functions.validateNotNull(new_credit.getInvoiceNo(), "Invoice Number method must not be empty");
        functions.validateNotNull(new_credit.getCost(), "Cost must not be empty");
        functions.validateNotNull(new_credit.getDescription(), "Description must not be empty");

        functions.validateNotNull(new_credit.getSiteId(), "Site must not be empty");
//        functions.validateNotNull(new_credit.isPaid(), "is Paid Boolean must not be empty");

        Site site = siteRepository.findByIdAndUserId(new_credit.getSiteId(), userId)
                .orElseThrow(() -> new RuntimeException("site not found"));


        if (new_credit.getDate().after(new Date())) {
            throw new IllegalArgumentException("Date must not be in the future");
        }

        if (new_credit.getCost().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Cost must be greater than zero");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Creditor creditor =  creditorRepository.findById(new_credit.getCreditorId())
                .orElseThrow(() -> new RuntimeException("Creditor not found"));

        Credit credit = new Credit();
        credit.setDate(new_credit.getDate());
        credit.setInvoiceNo(new_credit.getInvoiceNo());
        credit.setCost(new_credit.getCost());
        credit.setDescription(new_credit.getDescription());
        credit.setDateRecorded(new Date());
        credit.setDateEdited(null);
        credit.setSite(site);
        credit.setUser(user);
        credit.setCreditor(creditor);
        credit.setPaid(false); // please for this put it in condition first so that it creates a transaction in cash purchases and deduct from account


        creditRepository.save(credit);
        return creditRepository.findByUserIdAndIsPaid(userId, false);
    }

    @Transactional
    public List<Credit> payCreditor(CashRequest cash_req, HttpSession session){
        Integer userId = userService.getUserIdFromSession(session);
        Credit credit = creditRepository.findByIdAndUserId(cash_req.getCreditId(), userId)
                .orElseThrow(() -> new RuntimeException("credit transaction not found"));

        System.out.println(credit.getDate());
        cash_req.setDate(credit.getDate());
        cash_req.setPaymentMethod("bank account");
        cash_req.setCost(credit.getCost());
        cash_req.setDescription(credit.getDescription());
//        cash_req.setisc(true); IDK WHAT I DID
        cash_req.setCredit(credit);

        cashService.createCash(cash_req, session);


        credit.setPaid(true);

        return creditRepository.findByUserIdAndIsPaid(userId, false);
    }


}
