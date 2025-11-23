package com.example.ragchat.exception;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	private ResponseEntity<ApiError> build(HttpStatus status, String code, String message, String path) {
		ApiError err = ApiError.builder().message(message).errorCode(code).timestamp(Instant.now())
				.status(status.value()).path(path).build();
		return ResponseEntity.status(status).body(err);
	}

	// -------- CUSTOM EXCEPTIONS --------

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ApiError> handleUserNotFound(UserNotFoundException ex, HttpServletRequest req) {
		log.warn("User not found: {}", ex.getMessage());
		return build(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", ex.getMessage(), req.getRequestURI());
	}

	@ExceptionHandler(SessionNotFoundException.class)
	public ResponseEntity<ApiError> handleSessionNotFound(SessionNotFoundException ex, HttpServletRequest req) {
		return build(HttpStatus.NOT_FOUND, "SESSION_NOT_FOUND", ex.getMessage(), req.getRequestURI());
	}

	// ---------- VALIDATION ERRORS -----------

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
		String msg = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
		return build(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", msg, req.getRequestURI());
	}

	// -------- GENERIC RUNTIME -----------

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ApiError> handleRuntime(RuntimeException ex, HttpServletRequest req) {
		return build(HttpStatus.BAD_REQUEST, "RUNTIME_EXCEPTION", ex.getMessage(), req.getRequestURI());
	}

	// -------- CATCH ALL -----------

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiError> handleException(Exception ex, HttpServletRequest req) {
	    log.error("Unhandled error at path={}: {}", req.getRequestURI(), ex.getMessage(), ex);
		return build(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", ex.getMessage(), req.getRequestURI());
	}
}
