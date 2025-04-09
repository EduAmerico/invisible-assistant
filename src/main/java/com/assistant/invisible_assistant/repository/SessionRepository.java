package com.assistant.invisible_assistant.repository;

import com.assistant.invisible_assistant.model.Session;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SessionRepository extends JpaRepository<Session, Long> { 
    @Query("SELECT s FROM Session s LEFT JOIN FETCH s.screenshots WHERE s.id = :id")
    Optional<Session> findByIdWithScreenshots(@Param("id") Long id);
}
