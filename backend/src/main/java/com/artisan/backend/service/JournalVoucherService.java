package com.artisan.backend.service;

import com.artisan.backend.DTO.JournalVoucherDTO;
import com.artisan.backend.model.AccountMaster;
import com.artisan.backend.model.JournalVoucher;
import com.artisan.backend.model.User;
import com.artisan.backend.repository.AccountMasterRepository;
import com.artisan.backend.repository.JournalVoucherRepository;
import com.artisan.backend.repository.SiteRepository;
import com.artisan.backend.repository.UserRepository;
import com.artisan.backend.utility.Functions;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class JournalVoucherService {

    @Autowired
    private JournalVoucherRepository journalVoucherRepository;

    @Autowired
    private AccountMasterRepository accountMasterRepository;

    @Autowired
    private SiteRepository siteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private Functions functions;

    public List<JournalVoucher> getJournalVouchers(Pageable pageable, Integer siteId, Integer userId){
        int offset = pageable.getPageNumber() * pageable.getPageSize(); // Calculate the offset
        return journalVoucherRepository.findAllBySiteIdAndUserIdOrderByDateRecordedDesc(siteId, userId, pageable.getPageSize(), offset);

//        return journalVoucherRepository
//                .findAllBySiteIdAndUserIdOrderByDateRecordedDesc(siteId, pageable, userId);
    }

    @Transactional
    public List<JournalVoucher> createJournalVouchers(JournalVoucherDTO jvRequest, Integer userId){
        functions.validateNotNull(jvRequest.getDrMaster().getId(), "Account Master for DR must not be empty");
        functions.validateNotNull(jvRequest.getCrMaster().getId(), "Account Master for CR must not be empty");
        functions.validateNotNull(jvRequest.getSite().getId(), "Site must not be empty");
        functions.validateNotNull(userId, "User must not be empty");
        functions.validateNotNull(jvRequest.getAmount(), "Amount must not be empty");
        functions.isValidBigDecimal(jvRequest.getAmount());
        functions.validateNotNull(jvRequest.getDate(), "Date must not be empty");
        functions.validateNotNull(jvRequest.getDescription(), "Description must not be empty");
        functions.objectDoesNotExist(
                accountMasterRepository.existsById(jvRequest.getCrMaster().getId()),
                "Account Master for CR does not Exist");
        functions.objectDoesNotExist(
                accountMasterRepository.existsById(jvRequest.getDrMaster().getId()),
                "Account Master for DR does not Exist");
        functions.objectDoesNotExist(
                siteRepository.existsById(jvRequest.getSite().getId()),
                "Site does not Exist");

        functions.objectDoesNotExist(
                userRepository.existsById(userId),
                "User does not Exist");

        User user = User.builder().id(userId).build();

        JournalVoucher journalVoucher = JournalVoucher.builder()
                .crMaster(jvRequest.getCrMaster())
                .drMaster(jvRequest.getDrMaster())
                .site(jvRequest.getSite())
                .amount(new BigDecimal(jvRequest.getAmount()))
                .user(user)
                .date(jvRequest.getDate())
                .description(jvRequest.getDescription())
                .dateRecorded(new Date())
                .dateEdited(null)
                .build();

        journalVoucherRepository.save(journalVoucher);
//        journalVoucherRepository.flush();
//        journalVoucherRepository.refresh(journalVoucher);

        Pageable pageable = PageRequest.of(jvRequest.getPage(), jvRequest.getSize());

        return getJournalVouchers(pageable, jvRequest.getSite().getId(), userId);
    }
}
