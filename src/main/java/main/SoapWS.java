package main;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService
// говорим, что веб-сервис будет использоваться для вызова методов
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface SoapWS {
    // говорим, что этот метод можно вызывать удаленно
    @WebMethod
    public String getHelloString(String name);
}
