package com.example.ragchat.security;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

class ApiKeyFilterTest {

	private ApiKeyFilter apiKeyFilter;

	@Mock
	private HttpServletRequest request;

	@Mock
	private HttpServletResponse response;

	@Mock
	private FilterChain filterChain;

	@BeforeEach
	void setup() throws Exception {
		MockitoAnnotations.openMocks(this);

		apiKeyFilter = new ApiKeyFilter();

		// Inject values into @Value fields
		java.lang.reflect.Field apiKeyField = ApiKeyFilter.class.getDeclaredField("apiKey");
		apiKeyField.setAccessible(true);
		apiKeyField.set(apiKeyFilter, "SECRET123");

		java.lang.reflect.Field devModeField = ApiKeyFilter.class.getDeclaredField("devMode");
		devModeField.setAccessible(true);
		devModeField.set(apiKeyFilter, false);
	}

	@Test
	void testAllowsHealthEndpointWithoutKey() throws Exception {
		when(request.getRequestURI()).thenReturn("/api/v1/health");

		apiKeyFilter.doFilterInternal(request, response, filterChain);

		verify(filterChain).doFilter(request, response);
		verify(response, never()).sendError(anyInt(), anyString());
	}

	@Test
	void testBlocksMissingApiKey() throws Exception {
		when(request.getRequestURI()).thenReturn("/api/v1/chat");
		when(request.getHeader("X-API-KEY")).thenReturn(null);

		apiKeyFilter.doFilterInternal(request, response, filterChain);

		verify(response).sendError(eq(403), eq("Invalid API Key Found"));
		verify(filterChain, never()).doFilter(any(), any());
	}

	@Test
	void testBlocksInvalidApiKey() throws Exception {
		when(request.getRequestURI()).thenReturn("/api/v1/chat");
		when(request.getHeader("X-API-KEY")).thenReturn("WRONG_KEY");

		apiKeyFilter.doFilterInternal(request, response, filterChain);

		verify(response).sendError(eq(403), eq("Invalid API Key Found"));
		verify(filterChain, never()).doFilter(any(), any());
	}

	@Test
	void testAllowsValidApiKey() throws Exception {
		when(request.getRequestURI()).thenReturn("/api/v1/chat");
		when(request.getHeader("X-API-KEY")).thenReturn("SECRET123");

		apiKeyFilter.doFilterInternal(request, response, filterChain);

		verify(filterChain).doFilter(request, response);
		verify(response, never()).sendError(anyInt(), anyString());
	}
}
