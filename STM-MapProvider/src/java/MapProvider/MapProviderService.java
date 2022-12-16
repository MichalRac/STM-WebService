/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/WebService.java to edit this template
 */
package MapProvider;

import java.util.Base64;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author racmi
 */
@WebService(serviceName = "MapProviderService", targetNamespace = "MapProvider")
public class MapProviderService {

    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "hello")
    public String hello(@WebParam(name = "name") String txt) {
        return "Hello " + txt + " !";
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getEncodedMap")
    public String getEncodedMap(@WebParam(name = "lat0") double lat0, @WebParam(name = "long0") double long0, @WebParam(name = "lat1") double lat1, @WebParam(name = "long1") double long1) {
        //TODO write your implementation code here:
        String testString = "hello world";
        String encodedString = Base64.getEncoder().encodeToString(testString.getBytes());
        return encodedString;
    }
}
