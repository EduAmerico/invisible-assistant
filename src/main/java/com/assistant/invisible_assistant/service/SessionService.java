package com.assistant.invisible_assistant.service;

import com.assistant.invisible_assistant.model.Session;
import com.assistant.invisible_assistant.repository.SessionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;
    private Session currentSession;

    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
        this.currentSession = createNewSession();
    }

    private Session createNewSession() {
        Session session = new Session();
        session.setStartedAt(LocalDateTime.now());
        return sessionRepository.save(session);
    }

    public Session getCurrentSession() {
        return currentSession;
    }

    public void resetSession() {
        this.currentSession = createNewSession();
    }
}
