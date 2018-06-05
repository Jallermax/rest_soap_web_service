package com.aplana.apiPractice;

import java.time.LocalDateTime;

public class SessionManager {

    private String sessionId;
    private LocalDateTime creationDate;
    private ProfileManager profileManager;

    public SessionManager(String sessionId, ProfileManager profileManager) {
        this.sessionId = sessionId;
        this.creationDate = LocalDateTime.now();
        this.profileManager = profileManager;
    }

    public String getSessionId() {
        return sessionId;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public ProfileManager getProfileManager() {
        return profileManager;
    }

    public boolean isOld() {
        return SoapWSImpl.soapSession.equals(sessionId)
                ? LocalDateTime.now().minusHours(1).isAfter(creationDate)
                : LocalDateTime.now().minusDays(7).isAfter(creationDate);
    }
}
