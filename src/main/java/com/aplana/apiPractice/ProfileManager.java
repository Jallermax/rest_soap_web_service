package com.aplana.apiPractice;

import com.aplana.apiPractice.exceptions.ElementExistException;
import com.aplana.apiPractice.models.Profile;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class ProfileManager {

    private AtomicLong idGenerator;
    private Map<Long, Profile> profiles = new HashMap<>();

    final public static String idTypeRegex = "[1-9][0-9]{5}";

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

    public void addNewProfile(Profile profile) throws ElementExistException {
        profile.updateDynamicParams();
        if (profiles.get(profile.getId()) != null) {
            throw new ElementExistException("Profile with id " + profile.getId() + " already exists!");
        } else if (profiles.values()
                .stream()
                .anyMatch(profile1 -> profile1.getFullName().equals(profile.getFullName()))) {
            throw new ElementExistException("Profile with full name \"" + profile.getFullName() + "\" already exists!");
        }
        profiles.put(profile.getId(), profile);
    }

    public Profile removeProfile(Long id) {
        return profiles.remove(id);
    }

    public Profile getProfile(Long id) {
        return profiles.get(id);
    }

    public Map<Long, Profile> getProfiles() {
        return profiles;
    }
}
