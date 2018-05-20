package com.aplana.apiPractice;

import java.time.LocalDateTime;

public class SessionManager {

    private String sessionId;
    private LocalDateTime creationDate;

    public SessionManager(String sessionId) {
        this.sessionId = sessionId;
        this.creationDate = LocalDateTime.now();
    }

    public String getSessionId() {
        return sessionId;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public boolean isOld() {
        return LocalDateTime.now().minusDays(7).isAfter(creationDate);
    }
}
