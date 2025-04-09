package com.assistant.invisible_assistant.service;

import com.assistant.invisible_assistant.model.Session;
import com.assistant.invisible_assistant.repository.SessionRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

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

    @Transactional
    public Session getCurrentSessionWithScreenshots() {
        Session session = getCurrentSession();
        Session refreshed = sessionRepository.findByIdWithScreenshots(session.getId())
                .orElse(session);
        System.out.println("Screenshots count after reloading: " + refreshed.getScreenshots().size());
        return refreshed;
    }

    public void resetSession() {
        this.currentSession = createNewSession();
    }

    public Optional<Session> findById(Long id) {
        return sessionRepository.findById(id);
    }

}
