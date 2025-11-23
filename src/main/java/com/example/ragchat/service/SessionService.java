package com.example.ragchat.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.ragchat.dto.CreateSessionRequest;
import com.example.ragchat.entity.Session;
import com.example.ragchat.exception.SessionNotFoundException;
import com.example.ragchat.repository.SessionRepository;
import com.example.ragchat.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SessionService {
	private final SessionRepository sessionRepository;
	private final UserRepository userRepository;

	public Session createSession(CreateSessionRequest req) {
		Session s = new Session();
		s.setTitle(req.getTitle());
		if (req.getUserId() != null) {
			userRepository.findById(req.getUserId()).ifPresent(s::setUser);
		}
		return sessionRepository.save(s);
	}

	public Session rename(String id, String title) {
		Session s = sessionRepository.findById(id).orElseThrow(() -> new SessionNotFoundException("Not found"));
		s.setTitle(title);
		return sessionRepository.save(s);
	}

	public Session favorite(String id, boolean favorite) {
		Session s = sessionRepository.findById(id).orElseThrow(() -> new SessionNotFoundException("Not found"));
		s.setFavorite(favorite);
		return sessionRepository.save(s);
	}

	public void delete(String id) {
		sessionRepository.deleteById(id);
	}

	public List<Session> listByUser(String userId) {
		return sessionRepository.findByUserId(userId);
	}
}
