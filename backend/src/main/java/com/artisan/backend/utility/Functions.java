package com.artisan.backend.utility;

import com.artisan.backend.exceptions.UnhandledRejection;
import org.springframework.stereotype.Component;

@Component
public class Functions {

    public void validateNotNull(Object value, String message) {
        if (value == null) {
            throw new UnhandledRejection(message);
        }
    }
}
