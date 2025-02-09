package com.artisan.backend.utility;

import com.artisan.backend.exceptions.UnhandledRejection;
import com.artisan.backend.model.Type;
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

    public static void isValidEnum(String input, String message) {
        for (Type type : Type.values()) {
            if (type.name().equals(input)) {
                return;
            }
        }
        throw new UnhandledRejection(message);

    }


}
