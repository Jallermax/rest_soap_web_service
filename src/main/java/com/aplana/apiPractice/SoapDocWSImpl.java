package com.aplana.apiPractice;

import javax.jws.WebService;

@WebService(endpointInterface = "com.aplana.apiPractice.SoapDocWS")
public class SoapDocWSImpl implements SoapDocWS {

    @Override
    public String getWelcomeMessage(String name) {
        return "Hello, " + name;
    }
}
