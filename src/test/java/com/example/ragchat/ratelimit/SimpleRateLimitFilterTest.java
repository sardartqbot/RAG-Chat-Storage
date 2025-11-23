package com.example.ragchat.ratelimit;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

class SimpleRateLimitFilterTest {

	private SimpleRateLimitFilter filter;

	@Mock
	private HttpServletRequest request;

	@Mock
	private HttpServletResponse response;

	@Mock
	private FilterChain filterChain;

	@Mock
	private PrintWriter writer;

	@BeforeEach
	void setup() throws Exception {
		MockitoAnnotations.openMocks(this);
		filter = new SimpleRateLimitFilter();

		when(request.getRemoteAddr()).thenReturn("127.0.0.1");
		when(response.getWriter()).thenReturn(writer);
	}

	@Test
	void testAllowsRequestWithinLimit() throws Exception {
		// First request → should be allowed
		filter.doFilterInternal(request, response, filterChain);

		verify(filterChain, times(1)).doFilter(request, response);
		verify(response, never()).setStatus(429);
	}

	@Test
	void testBlocksAfterExceedingRateLimit() throws Exception {
		// Access private field "max" = 100, but instead of reflection we simply loop

		for (int i = 0; i < 100; i++) {
			filter.doFilterInternal(request, response, filterChain);
		}

		// 101st request → should be blocked
		filter.doFilterInternal(request, response, filterChain);

		verify(response).setStatus(429);
		verify(writer).write("Too many requests");
		verify(filterChain, times(100)).doFilter(request, response); // only first 100 allowed
	}
}
