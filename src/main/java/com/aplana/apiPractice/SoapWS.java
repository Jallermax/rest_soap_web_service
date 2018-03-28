package com.aplana.apiPractice;

import models.Profile;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.util.List;
import java.util.UUID;

@WebService
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT)
public interface SoapWS {
    @WebMethod
    String getWelcomeMessage(@WebParam(name = "Name") String name);

    @WebMethod
    String getUniqueKey();

    @WebMethod
    UUID getUUID(@WebParam(name = "param") List<String> list);

    @WebMethod
    List<Profile> getProfileList();

    @WebMethod
    Profile getProfile(Long id);

    @WebMethod
    String addProfile(@WebParam(name = "Profile") Profile profile);

    @WebMethod
    String removeProfile (Long id);
}
