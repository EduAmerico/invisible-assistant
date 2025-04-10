package com.assistant.invisible_assistant.repository;

import com.assistant.invisible_assistant.model.Screenshot;
import com.assistant.invisible_assistant.model.Session;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ScreenshotRepository extends JpaRepository<Screenshot, Long> {
    
    @Query("SELECT s FROM Screenshot s WHERE s.session = :session AND s.analyzed = false ORDER BY s.capturedAt ASC")
    List<Screenshot> findUnanalyzedBySession(@Param("session") Session session);
}