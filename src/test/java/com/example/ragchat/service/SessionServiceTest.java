package com.example.ragchat.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import com.example.ragchat.dto.CreateSessionRequest;
import com.example.ragchat.entity.Session;
import com.example.ragchat.entity.User;
import com.example.ragchat.exception.SessionNotFoundException;
import com.example.ragchat.repository.SessionRepository;
import com.example.ragchat.repository.UserRepository;
import com.example.ragchat.service.SessionService;

class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SessionService sessionService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateSession() {
        CreateSessionRequest req = new CreateSessionRequest();
        req.setTitle("My Chat");
        req.setUserId("u1");

        User user = new User();
        user.setId("u1");

        when(userRepository.findById("u1")).thenReturn(Optional.of(user));

        Session session = new Session();
        session.setId("s1");
        session.setTitle("My Chat");
        session.setUser(user);

        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        Session created = sessionService.createSession(req);

        assertEquals("My Chat", created.getTitle());
        assertEquals(user, created.getUser());
    }

    @Test
    void testRename() {
        Session session = new Session();
        session.setId("s1");
        session.setTitle("Old Title");

        when(sessionRepository.findById("s1")).thenReturn(Optional.of(session));
        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        Session renamed = sessionService.rename("s1", "New Title");

        assertEquals("New Title", renamed.getTitle());
    }

    @Test
    void testRename_NotFound() {
        when(sessionRepository.findById("bad-id")).thenReturn(Optional.empty());

        assertThrows(SessionNotFoundException.class,
                () -> sessionService.rename("bad-id", "X"));
    }
}
