package com.example.ragchat.logging;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain)
			throws ServletException, IOException {

		long start = System.currentTimeMillis();

		String method = req.getMethod();
		String path = req.getRequestURI();
		String ip = req.getRemoteAddr();

		filterChain.doFilter(req, res);

		long duration = System.currentTimeMillis() - start;

		log.info("Request: method={}, path={}, ip={}, status={}, durationMs={}", method, path, ip, res.getStatus(),
				duration);
	}
}
