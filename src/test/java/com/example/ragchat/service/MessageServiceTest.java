package com.example.ragchat.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import com.example.ragchat.dto.MessageRequest;
import com.example.ragchat.entity.Message;
import com.example.ragchat.entity.Sender;
import com.example.ragchat.entity.Session;
import com.example.ragchat.repository.MessageRepository;
import com.example.ragchat.repository.SessionRepository;
import com.example.ragchat.service.MessageService;

class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private SessionRepository sessionRepository;

    @InjectMocks
    private MessageService messageService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // ----------------------------------------------------------------------------------
    // TEST addMessage()
    // ----------------------------------------------------------------------------------

    @Test
    void testAddMessage_Success() {
        // Mock session
        Session session = new Session();
        session.setId("s1");

        when(sessionRepository.findById("s1")).thenReturn(Optional.of(session));

        // Build request (context is already initialized inside DTO)
        MessageRequest req = new MessageRequest();
        req.setSender(Sender.USER);
        req.setContent("Hello world");

        // Mock returned saved message
        Message saved = new Message();
        saved.setId("m1");
        saved.setSession(session);
        saved.setSender(Sender.USER);
        saved.setContent("Hello world");
        saved.setContext(req.getContext());

        when(messageRepository.save(any(Message.class))).thenReturn(saved);

        // Execute
        Message result = messageService.addMessage("s1", req);

        // Verify correct behavior
        assertEquals(Sender.USER, result.getSender());
        assertEquals("Hello world", result.getContent());
        assertEquals(session, result.getSession());
        assertNotNull(result.getContext());       // must always be initialized
        assertTrue(result.getContext().isEmpty()); // default empty map
    }

    @Test
    void testAddMessage_SessionNotFound() {
        when(sessionRepository.findById("unknown")).thenReturn(Optional.empty());

        MessageRequest req = new MessageRequest();
        req.setSender(Sender.USER);
        req.setContent("Hello");

        assertThrows(RuntimeException.class,
                () -> messageService.addMessage("unknown", req));
    }

    // ----------------------------------------------------------------------------------
    // TEST listMessages()
    // ----------------------------------------------------------------------------------

    @Test
    void testListMessages() {
        Page<Message> mockPage = new PageImpl<>(java.util.List.of(new Message()));

        when(messageRepository.findBySessionId(eq("s1"), any(PageRequest.class)))
                .thenReturn(mockPage);

        Page<Message> result = messageService.listMessages("s1", 0, 10);

        assertEquals(1, result.getContent().size());
    }
}
