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
        Integer userId = userService.getUserIdFromSession(session);
        return siteRepository.findByUserIdAndIsArchived(userId, false);
    }

    @Transactional
    public List<Site> deleteSite(Integer siteId, HttpSession session) {
        Integer userId = userService.getUserIdFromSession(session);

        if (siteId == null) {
            throw new UnhandledRejection("Site Id name must not be empty");
        }

        boolean siteExists = siteRepository.existsByIdAndUserId(siteId, userId);
        if(!siteExists){
            throw new UnhandledRejection("Site does not exist!");
        }

        siteRepository.archiveSiteById(siteId);

        return siteRepository.findByUserIdAndIsArchived(userId, false);
    }

    @Transactional
    public List<Site> createSite(Site site, HttpSession session) {
        Integer userId = userService.getUserIdFromSession(session);

        if (site.getName() == null || site.getName().trim().isEmpty()) {
            throw new UnhandledRejection("Site name must not be empty");
        }

        if (site.getDescription() == null || site.getDescription().trim().isEmpty()) {
            throw new UnhandledRejection("Site description must not be empty");
        }

        if (siteRepository.existsByNameAndUserId(site.getName(), userId)) {
            throw new UnhandledRejection("Site already exists");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        site.setUser(user);
        site.setArchived(false);
        siteRepository.save(site);

        return siteRepository.findByUserIdAndIsArchived(userId, false);
    }

    @Transactional
    public List<Site> editSite(Site edited_site, HttpSession session) {
        Integer userId = userService.getUserIdFromSession(session);

        if (edited_site.getName() == null || edited_site.getName().trim().isEmpty()) {
            throw new UnhandledRejection("Site name must not be empty");
        }

        if (edited_site.getDescription() == null || edited_site.getDescription().trim().isEmpty()) {
            throw new UnhandledRejection("Site description must not be empty");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
//        if (siteRepository.existsByNameAndUserIdAndNotId(edited_site.getName(), user, edited_site.getId())) {
//            throw new IllegalArgumentException("Site already exists");
//        }

        Site site = siteRepository.findById(edited_site.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        site.setName(edited_site.getName());
        site.setDescription(edited_site.getDescription());
//        site.setName(name);
        siteRepository.save(site);

        return siteRepository.findByUserIdAndIsArchived(userId, false);
    }

}
