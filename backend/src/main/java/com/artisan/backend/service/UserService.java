package com.artisan.backend.service;

import com.artisan.backend.DTO.JsonMapper;
import com.artisan.backend.DTO.UserId;
import com.artisan.backend.exceptions.UnhandledRejection;
import com.artisan.backend.model.User;
import com.artisan.backend.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class UserService {

    @Autowired
    private  JsonMapper jsonMapper;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public UserId registerUser(User username, HttpSession session) {
        // Step 1: Validate the username is not empty
        if (username == null || username.getUsername().trim().isEmpty()) {
            throw new UnhandledRejection("Username cannot be empty");
        }

        // Step 2: Check if the username contains only alphabetic characters and numbers
        String regex = "^[a-zA-Z][a-zA-Z0-9]*$";
        if (!Pattern.matches(regex, username.getUsername())) {
            throw new UnhandledRejection("Username can only contain letters and numbers");
        }

        // Step 3: Check if the username already exists in the database
        if (userRepository.existsByUsername(username.getUsername())) {
            throw new UnhandledRejection("Username already exists");
        }

        // Step 4: Save the new user in the database
        User user = new User(username.getUsername());
        user = userRepository.save(user);

        // Step 5: Store the user ID in the session
        session.setAttribute("userId", user.getId());  // Store the user ID in the session

        return jsonMapper.convertUserTdToJson(getUserIdFromSession(session));
    }

    // Retrieve the user ID from the session
    public Integer getUserIdFromSession(HttpSession session) {
        return (Integer) session.getAttribute("userId");
    }

    public UserId loginUser(User username, HttpSession session) {
        if (username == null || username.getUsername().trim().isEmpty()) {
            throw new UnhandledRejection("Username cannot be empty");
        }

        User user = userRepository.findByUsername(username.getUsername());

        if (user != null) {
            session.setAttribute("userId", user.getId());  // Store the user ID in the session
        } else {
            throw new UnhandledRejection("User not found");
        }

        session.setAttribute("userId", user.getId());

        return jsonMapper.convertUserTdToJson(getUserIdFromSession(session));
    }

}
