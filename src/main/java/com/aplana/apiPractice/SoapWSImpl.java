package com.aplana.apiPractice;

import models.Profile;

import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@WebService(endpointInterface = "com.aplana.apiPractice.SoapWS")
public class SoapWSImpl implements SoapWS {

    @Override
    public String getWelcomeMessage(String name) {
        return "Hello, " + name;
    }

    @Override
    public String getUniqueKey() {
        return UUID.randomUUID().toString();
    }

    @Override
    public UUID getUUID(List<String> list) {
        System.out.println(list.toString());
        return UUID.randomUUID();
    }

    @Override
    public List<Profile> getProfileList() {
        return new ArrayList<>(ProfileManager.getInstance().getProfiles().values());
    }

    @Override
    public Profile getProfile(Long id) {
        return ProfileManager.getInstance().getProfile(id);
    }
//
//    @Override
//    public HashMap<Long, Profile> getProfileList() {
//        return ProfileManager.getInstance().getProfiles();
//    }

    @Override
    public String addProfile(Profile profile) {
        ProfileManager.getInstance().addNewProfile(profile);
        return "Profile with id: " + profile.getId() +" was successfully created.";
    }

    @Override
    public String removeProfile(Long id) {
        ProfileManager.getInstance().removeProfile(id);
        return "Profile with id: " + id + " was removed";
    }
}
