package com.aplana.apiPractice;

import com.aplana.apiPractice.models.AddProfileRq;
import com.aplana.apiPractice.models.Profile;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.util.List;

@WebService
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT)
public interface SoapWS {
    @WebMethod
    String getWelcomeMessage(@WebParam(name = "Name") String name);

    @WebMethod
    List<Profile> getProfileList();

    @WebMethod
    Profile getProfile(Long id);

    @WebMethod
    String addProfile(@WebParam(name = "ProfileRq") AddProfileRq profileRq);

    @WebMethod
    String removeProfile (Long id);

    @WebMethod
    String getTaskKey ();
}
