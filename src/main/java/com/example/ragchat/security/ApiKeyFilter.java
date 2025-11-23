package com.example.ragchat.security;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ApiKeyFilter extends OncePerRequestFilter {

    @Value("${app.api-key}")
    private String apiKey;

    @Value("${app.dev-mode:false}")
    private boolean devMode;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println("---- Incoming request ----");
        System.out.println("URI: " + request.getRequestURI());
        System.out.println("Method: " + request.getMethod());

        String requestKey = request.getHeader("X-API-KEY");
        System.out.println("X-API-KEY header: " + requestKey);
        System.out.println("Expected API Key: " + apiKey);

        // Skip health and swagger
        String path = request.getRequestURI();
        if (path.startsWith("/api/v1/health") || path.startsWith("/v3/api-docs") || path.startsWith("/swagger-ui")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (requestKey == null || !requestKey.equals(apiKey)) {
            System.out.println("Invalid API Key Found!");
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid API Key Found");
            return;
        }

        System.out.println("API Key validated successfully");
        filterChain.doFilter(request, response);
    }

}
