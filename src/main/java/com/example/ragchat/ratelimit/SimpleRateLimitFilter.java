package com.example.ragchat.ratelimit;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Simple in-memory rate limiter for demo. Replace with Redis-backed Bucket4j in production.
 */
@Component
public class SimpleRateLimitFilter extends OncePerRequestFilter {

    private final Map<String, Window> windows = new ConcurrentHashMap<>();
    private final int max = 100; // per window
    private final long windowMs = 60_000L;

    @Override
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String key = request.getRemoteAddr();
        Window w = windows.computeIfAbsent(key, k -> new Window(Instant.now().toEpochMilli()));
        synchronized (w) {
            long now = Instant.now().toEpochMilli();
            if (now - w.start >= windowMs) {
                w.start = now;
                w.count.set(0);
            }
            if (w.count.incrementAndGet() > max) {
                response.setStatus(429);
                response.getWriter().write("Too many requests");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private static class Window {
        long start;
        AtomicInteger count = new AtomicInteger(0);
        Window(long start) { this.start = start; }
    }
}
