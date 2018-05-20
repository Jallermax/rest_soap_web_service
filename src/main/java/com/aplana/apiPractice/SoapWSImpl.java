package com.aplana.apiPractice;

import com.aplana.apiPractice.exceptions.ElementExistException;
import com.aplana.apiPractice.models.Profile;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@WebService(endpointInterface = "com.aplana.apiPractice.SoapWS")
public class SoapWSImpl implements SoapWS {

    //TODO return errors in headers
    @Resource
    private WebServiceContext context;
    //TODO get SessionId from SOAPMessageContext via SOAPHandler (https://stackoverflow.com/questions/28487114/retrieving-soap-header-on-jaxws-server-side, https://www.mkyong.com/webservices/jax-ws/jax-ws-soap-handler-in-server-side/)
    private static final String soapSession = "soapSession";

    @Override
    public String getWelcomeMessage(String name) {
        return "Hello, " + name;
    }

//    @Override
//    public String getUniqueKey() {
//        return UUID.randomUUID().toString();
//    }
//
//    @Override
//    public UUID getUUID(List<String> list) {
//        System.out.println(list.toString());
//        return UUID.randomUUID();
//    }


    @Override
    public List<Profile> getProfileList() {
        return new ArrayList<>(ProfileManager.getInstance(soapSession).getProfiles().values());
    }

    @Override
    public Profile getProfile(Long id) {
        return ProfileManager.getInstance(soapSession).getProfile(id);
    }

    @Override
    public String addProfile(Profile profile) {
        Long id;
        try {
            id = ProfileManager.getInstance(soapSession).addNewProfile(profile);
            if (id == null) {
                return "Validation failed. Profile wasn't created. Use valid values.";
            }
        } catch (ElementExistException e) {
            return e.getMessage();
        }
        return "Profile with id: " + id +" was successfully created.";
    }

    @Override
    public String removeProfile(Long id) {
        final Profile removedProfile = ProfileManager.getInstance(soapSession).removeProfile(id);
        return removedProfile == null ? "Profile with id: " + id + " not found!"
                : "Profile with id: " + removedProfile.getId() + "(" + removedProfile.getFullName() + ") was removed";
    }

    @Override
    public String getTaskKey() {
        return new TaskValidation(TaskValidation.ServiceType.SOAP).checkValid(ProfileManager.getInstance(soapSession).getProfiles().values());
    }
}
