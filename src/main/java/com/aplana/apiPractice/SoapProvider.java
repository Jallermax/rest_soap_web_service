package com.aplana.apiPractice;

import javax.xml.soap.*;
import javax.xml.ws.Provider;
import javax.xml.ws.Service;
import javax.xml.ws.ServiceMode;
import javax.xml.ws.WebServiceProvider;

@ServiceMode(value = Service.Mode.MESSAGE)
@WebServiceProvider(
        serviceName = "SoapProviderService",
        portName = "SoapProviderPort"
)
public class SoapProvider implements Provider<SOAPMessage> {
    @Override
    public SOAPMessage invoke(SOAPMessage request) {
        try {
            System.out.println("SoapHeader:");
            request.getSOAPHeader().getAllAttributes().forEachRemaining((att)-> System.out.println("\nAttribute: " + att));


        //Build the SOAP Fault Response
        MessageFactory factory = MessageFactory.newInstance(SOAPConstants.SOAP_1_1_PROTOCOL);
        SOAPMessage response = factory.createMessage();

        SOAPFault fault = response.getSOAPBody().addFault();
        fault.setFaultString("OuOuOu! Error occured!");

        fault.addDetail().addChildElement("exception").addTextNode("999");
        return response;
        } catch (SOAPException e) {
            e.printStackTrace();
        }
        return null;
    }
}
