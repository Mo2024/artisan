package com.artisan.backend.DTO;
import com.artisan.backend.model.AccountMaster;
import com.artisan.backend.model.Site;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class JournalVoucherDTO {

    private  Integer id;

    private AccountMaster crMaster;

    private AccountMaster drMaster;

    private Site site;

    private String amount;

    private Date date;

    private String description;

    private Integer page;

    private Integer size;


}
