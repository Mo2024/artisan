package com.artisan.backend.controller;

import com.artisan.backend.DTO.JsonMapper;
import com.artisan.backend.DTO.UserId;
import com.artisan.backend.exceptions.UnhandledRejection;
import com.artisan.backend.model.User;
import com.artisan.backend.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.artisan.backend.DTO.ErrorResponse;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JsonMapper jsonMapper;

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody User username, HttpSession session){
        try {
            UserId userId = userService.registerUser(username, session);
            return ResponseEntity.ok().body(userId);
        } catch (UnhandledRejection e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("An error occurred while registering the user: " + e.getMessage()));
        }
    }

    @GetMapping("/isAuth")
    public ResponseEntity<?> getUserIdFromSession(HttpSession session) {
        try{
            UserId userId = jsonMapper.convertUserTdToJson(userService.getUserIdFromSession(session));
            return ResponseEntity.ok(userId);
        } catch (UnhandledRejection e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("An error occurred while authenticating the user: " + e.getMessage()));
        }

    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        try {
            // Invalidate the session
            session.invalidate();

            // Return a success message
            return ResponseEntity.ok("Logged out successfully");
        } catch (Exception e) {
            // Handle any unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("An error occurred while logging out: " + e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user, HttpSession session){
        try {
            UserId userId = userService.loginUser(user, session);
            return ResponseEntity.ok(userId);
        } catch (UnhandledRejection e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("An error occurred while logging in the user: " + e.getMessage()));
        }
    }
}
