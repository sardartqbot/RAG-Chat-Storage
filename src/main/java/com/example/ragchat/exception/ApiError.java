package com.example.ragchat.exception;

import java.time.Instant;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiError {
	private String message;
	private String errorCode;
	private Instant timestamp;
	private int status;
	private String path;
}
