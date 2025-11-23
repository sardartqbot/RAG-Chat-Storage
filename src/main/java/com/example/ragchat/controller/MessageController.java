package com.example.ragchat.controller;

import com.example.ragchat.dto.MessageRequest;
import com.example.ragchat.entity.Message;
import com.example.ragchat.service.MessageService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sessions/{sessionId}/messages")
@RequiredArgsConstructor
@SecurityRequirement(name = "api_key")
public class MessageController {
    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<Message> add(@PathVariable String sessionId, @RequestBody MessageRequest req) {
        return ResponseEntity.status(201).body(messageService.addMessage(sessionId, req));
    }

    @GetMapping
    public ResponseEntity<Page<Message>> list(@PathVariable String sessionId,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "50") int size) {
        return ResponseEntity.ok(messageService.listMessages(sessionId, page, size));
    }
}
