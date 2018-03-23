package main;

import javax.jws.WebService;

@WebService(endpointInterface = "main.SoapWS")
public class SoapWSImpl implements SoapWS {
    @Override
    public String getHelloString(String name) {
        return "Hello, " + name;
    }
}
