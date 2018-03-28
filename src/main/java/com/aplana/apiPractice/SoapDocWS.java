package com.aplana.apiPractice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService
@SOAPBinding()
public interface SoapDocWS {

    @WebMethod
    String getWelcomeMessage(@WebParam(name = "Name") String name);
}
