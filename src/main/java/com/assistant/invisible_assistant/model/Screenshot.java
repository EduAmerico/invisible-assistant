package com.assistant.invisible_assistant.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Screenshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String filePath;
    private LocalDateTime capturedAt = LocalDateTime.now();
    private boolean analyzed = false; // Indica se a screenshot já foi analisada pela IA

    @ManyToOne
    @JoinColumn(name = "session_id")
    private Session session;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public LocalDateTime getCapturedAt() {
        return capturedAt;
    }

    public void setCapturedAt(LocalDateTime capturedAt) {
        this.capturedAt = capturedAt;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
    
    public boolean isAnalyzed() {
        return analyzed;
    }
    
    public void setAnalyzed(boolean analyzed) {
        this.analyzed = analyzed;
    }
}