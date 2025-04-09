package com.assistant.invisible_assistant.repository;

import com.assistant.invisible_assistant.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, Long> { }
