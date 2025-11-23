package com.example.ragchat.exception;

public class SessionNotFoundException extends RuntimeException {
	public SessionNotFoundException(String id) {
		super("Session not found with id: " + id);
	}
}
