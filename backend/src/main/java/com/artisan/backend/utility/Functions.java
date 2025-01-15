package com.artisan.backend.utility;

import com.artisan.backend.exceptions.UnhandledRejection;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class Functions {

    public void validateNotNull(Object value, String message) {
        if (value == null) {
            throw new UnhandledRejection(message);
        }
    }

    public static boolean isValidBigDecimal(String value) {
        try {
            new BigDecimal(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
