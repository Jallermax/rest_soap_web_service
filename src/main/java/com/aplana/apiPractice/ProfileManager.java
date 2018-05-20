package com.aplana.apiPractice;

import com.aplana.apiPractice.exceptions.ElementExistException;
import com.aplana.apiPractice.models.Profile;
import com.aplana.apiPractice.models.Project;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class ProfileManager {

    private static final Logger LOG = Log.getLogger(ProfileManager.class);
    public static final String idTypeRegex = "[1-9][0-9]{5}";
    //    private static ProfileManager ourInstance = new ProfileManager();
    private static Set<SessionManager> sessionManagerSet = new HashSet<>();
    //TODO cleanup old sessions
    private static Map<String, ProfileManager> sessionProfileManagers = new HashMap<>();

    private String sessionId;
    private AtomicLong idGenerator;
    private Map<Long, Profile> profiles = new HashMap<>();

    private ProfileManager() {
        idGenerator = new AtomicLong(100000);
    }

    public static ProfileManager getInstance(String sessionId) {
//        return ourInstance;
        cleanUpOldSessions();
        return sessionProfileManagers.containsKey(sessionId)
                ? sessionProfileManagers.get(sessionId)
                : addNewManager(sessionId);
    }

    public static Set<SessionManager> getSessionManagerSet() {
        return sessionManagerSet;
    }

    private static void cleanUpOldSessions() {
        List<SessionManager> sessionList = new ArrayList<>();
        for (SessionManager session : sessionManagerSet) {
            if (session.isOld()) {
                sessionList.add(session);
            }
        }
        for (SessionManager session : sessionList) {

            LOG.info("Removed old session [id:" + session.getSessionId() + ", creationDate:" + session.getCreationDate().toString() + "]");
            sessionProfileManagers.remove(session.getSessionId());
            sessionManagerSet.remove(session);
        }
    }

    private static ProfileManager addNewManager(String sessionId) {
        ProfileManager manager = new ProfileManager();
        SessionManager sessionManager = new SessionManager(sessionId);
        sessionManagerSet.add(sessionManager);
        manager.initTestProfiles();
        manager.sessionId = sessionId;
        sessionProfileManagers.put(sessionManager.getSessionId(), manager);
        return manager;
    }

    private void initTestProfiles() {

        Profile profile = new Profile();
        profile.setName("Федор");
        profile.setSecondName("Владимирович");
        profile.setSurName("Синицын");
        profile.setPosition("Старший уборщик по складу данных");
        profile.setBirthday(new GregorianCalendar(1989, 6, 1).getTime());
        profile.setEmail("imabird@gmail.com");
        Project project1 = new Project();
        project1.setName("Проект нагрузочных свидетелей Скраммастера");
        project1.setDescription("Работал с белым ящиком пандоры, общался с заказчиком, духами, внеземными цивилизациями. " +
                "В проекте использовались все буквы кирилического латинского алфавита, местами арабские цифры.");
        project1.setStartDate(new GregorianCalendar(2015, 3, 16).getTime());
        project1.setEndDate(new GregorianCalendar(2016, 2, 29).getTime());
        Project project2 = new Project();
        project2.setName("Проект 2");
        project2.setDescription("Описание 2.");
        project2.setStartDate(new GregorianCalendar(2016, 3, 5).getTime());
        profile.addProject(project1);
        profile.addProject(project2);
        try {
            this.addNewProfile(profile);
        } catch (ElementExistException e) {
            e.printStackTrace();
        }
    }

    public Long getNewId() {
        return idGenerator.incrementAndGet();
    }

    public Long addNewProfile(Profile rawProfile) throws ElementExistException {
        Profile profile = new Profile(getNewId(), rawProfile);
        profile.updateDynamicParams();
        if (profiles.get(profile.getId()) != null) {
            throw new ElementExistException("Profile with id " + profile.getId() + " already exists!");
        } else if (profiles.values()
                .stream()
                .anyMatch(profile1 -> profile1.getFullName().equals(profile.getFullName()))) {
            throw new ElementExistException("Profile with full name \"" + profile.getFullName() + "\" already exists!");
        }
        if (!profile.valid()) return null;
        profiles.put(profile.getId(), profile);
        return profile.getId();
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
