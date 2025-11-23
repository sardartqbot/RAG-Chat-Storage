package com.example.ragchat.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ragchat.dto.CreateSessionRequest;
import com.example.ragchat.dto.FavoriteRequest;
import com.example.ragchat.dto.RenameSessionRequest;
import com.example.ragchat.entity.Session;
import com.example.ragchat.service.SessionService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/sessions")
@RequiredArgsConstructor
@SecurityRequirement(name = "api_key")
public class SessionController {
	private final SessionService sessionService;

	@PostMapping
	public ResponseEntity<Session> create(@RequestBody CreateSessionRequest req) {
		return ResponseEntity.ok(sessionService.createSession(req));
	}

	@PatchMapping("/{id}/rename")
	public ResponseEntity<Session> rename(@PathVariable String id, @RequestBody RenameSessionRequest req) {
		return ResponseEntity.ok(sessionService.rename(id, req.getTitle()));
	}

	@PatchMapping("/{id}/favorite")
	public ResponseEntity<Session> favorite(@PathVariable String id, @RequestBody FavoriteRequest req) {
		return ResponseEntity.ok(sessionService.favorite(id, req.isFavorite()));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable String id) {
		sessionService.delete(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping
	public ResponseEntity<List<Session>> list(@RequestParam(name = "userId", required = false) String userId) {
		return ResponseEntity.ok(sessionService.listByUser(userId));
	}

}
