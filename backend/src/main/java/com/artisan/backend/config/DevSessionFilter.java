package com.artisan.backend.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class DevSessionFilter implements Filter {

    private final Environment environment;

    @Autowired
    public DevSessionFilter(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // Check if the active profile is "dev"
        String[] activeProfiles = environment.getActiveProfiles();
        boolean isDevProfile = false;

        for (String profile : activeProfiles) {
            if ("dev".equals(profile)) {
                isDevProfile = true;
                break;
            }
        }

        if (isDevProfile && request instanceof HttpServletRequest) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            HttpSession session = httpServletRequest.getSession();

            if (session.getAttribute("userId") == null) { // Only set if not already present
                session.setAttribute("userId", 1); // Store the user ID in the session
            }
        }

        // Continue the filter chain
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization logic if needed
    }

    @Override
    public void destroy() {
        // Cleanup logic if needed
    }
}
