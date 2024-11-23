package com.artisan.backend.service;

import com.artisan.backend.DTO.UserId;
import com.artisan.backend.exceptions.UnhandledRejection;
import com.artisan.backend.model.Site;
import com.artisan.backend.model.User;
import com.artisan.backend.repository.SiteRepository;
import com.artisan.backend.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class SiteService {

    @Autowired
    private SiteRepository siteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    public List<Site> getSites(HttpSession session){
        UserId userId = userService.getUserIdFromSession(session);
        return siteRepository.findByUserId(Integer.parseInt(userId.getUserId()));
    }

    @Transactional
    public List<Site> createSite(Site site, HttpSession session) {
        UserId userId = userService.getUserIdFromSession(session);

        System.out.println("Test: " + site);
        if (site.getName() == null || site.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Site name must not be empty");
        }

        if (site.getDescription() == null || site.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Site description must not be empty");
        }

        if (siteRepository.existsByNameAndUserId(site.getName(), Integer.parseInt(userId.getUserId()))) {
            throw new IllegalArgumentException("Site already exists");
        }

//        User user = new User(username.getUsername());

        User user = userRepository.findById(Integer.parseInt(userId.getUserId()))
                .orElseThrow(() -> new RuntimeException("User not found"));
        site.setUser(user);
        siteRepository.save(site);

        return siteRepository.findByUserId(Integer.parseInt(userId.getUserId()));
    }

}
