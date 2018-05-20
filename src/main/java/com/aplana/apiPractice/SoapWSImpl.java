package com.aplana.apiPractice;

import com.aplana.apiPractice.exceptions.ElementExistException;
import com.aplana.apiPractice.models.AddProfileRq;
import com.aplana.apiPractice.models.Profile;

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
    public List<Profile> getProfileList() {
        return new ArrayList<>(ProfileManager.getInstance().getProfiles().values());
    }

    @Override
    public Profile getProfile(Long id) {
        return ProfileManager.getInstance().getProfile(id);
    }

    @Override
    public String addProfile(AddProfileRq profileRq) {
        if (profileRq == null) {
            return "Incorrect request!";
        }
        Profile profile = new Profile(profileRq);
        try {
            ProfileManager.getInstance().addNewProfile(profile);
        } catch (ElementExistException e) {
            return e.getMessage();
        }
        return "Profile with id: " + profile.getId() +" was successfully created.";
    }

    @Override
    public String removeProfile(Long id) {
        final Profile removedProfile = ProfileManager.getInstance().removeProfile(id);
        return removedProfile == null ? "Profile with id: " + id + " not found!"
                : "Profile with id: " + removedProfile.getId() + "(" + removedProfile.getFullName() + ") was removed";
    }
}
