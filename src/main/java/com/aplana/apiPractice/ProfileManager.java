package com.aplana.apiPractice;

import models.Profile;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class ProfileManager {

    private AtomicLong idGenerator;
    private Map<Long, Profile> profiles = new HashMap<>();

    private static ProfileManager ourInstance = new ProfileManager();

    public static ProfileManager getInstance() {
        return ourInstance;
    }

    private ProfileManager() {
        idGenerator = new AtomicLong(100000);
    }

    public Long getNewId() {
        return idGenerator.incrementAndGet();
    }

    public void addNewProfile(Profile profile) {
        profiles.put(profile.getId(), profile);
    }

    public void removeProfile(Long id) {
        profiles.remove(id);
    }

    public Profile getProfile(Long id) {
        return profiles.get(id);
    }

    public Map<Long, Profile> getProfiles() {
        return profiles;
    }
}
