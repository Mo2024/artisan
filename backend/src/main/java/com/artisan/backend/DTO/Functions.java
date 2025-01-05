package com.artisan.backend.DTO;

import java.math.BigDecimal;

public class Functions {
    public static boolean isValidBigDecimal(String value) {
        try {
            new BigDecimal(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
