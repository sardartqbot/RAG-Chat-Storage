package com.example.ragchat.service;

import com.example.ragchat.dto.MessageRequest;
import com.example.ragchat.entity.Message;
import com.example.ragchat.entity.Session;
import com.example.ragchat.repository.MessageRepository;
import com.example.ragchat.repository.SessionRepository;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final SessionRepository sessionRepository;

    public Message addMessage(String sessionId, MessageRequest req) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        Message m = new Message();
        m.setSession(session);
        m.setSender(req.getSender());
        m.setContent(req.getContent());

        // Always ensure it's a Map
        m.setContext(req.getContext() != null ? req.getContext() : new HashMap<>());

        return messageRepository.save(m);
    }


    public Page<Message> listMessages(String sessionId, int page, int size) {
        return messageRepository.findBySessionId(sessionId, PageRequest.of(page, size));
    }
}
