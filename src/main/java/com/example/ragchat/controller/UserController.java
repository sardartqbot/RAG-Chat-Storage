package com.example.ragchat.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ragchat.dto.CreateUserRequest;
import com.example.ragchat.entity.User;
import com.example.ragchat.exception.UserNotFoundException;
import com.example.ragchat.repository.UserRepository;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "api_key")
public class UserController {

	private final UserRepository userRepository;

	@PostMapping
	public User createUser(@RequestBody CreateUserRequest req) {
		User user = new User();
		user.setEmail(req.getEmail());
		return userRepository.save(user);
	}

	@GetMapping("/{id}")
	public User getUser(@PathVariable String id) {
		return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
	}

}
