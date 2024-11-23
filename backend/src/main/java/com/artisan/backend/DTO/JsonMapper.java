package com.artisan.backend.DTO;

import org.springframework.stereotype.Component;

@Component
public class JsonMapper {
    public UserId convertUserTdToJson(Integer userId) {
        return new UserId(userId);
    }
}
