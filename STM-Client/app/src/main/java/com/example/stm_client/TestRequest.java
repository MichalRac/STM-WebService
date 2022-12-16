package com.example.stm_client;

import android.os.Handler;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import Marshals.MarshalDouble;

public class TestRequest {
    private static final String NAMESPACE = "https://192.168.0.32:8080/"; // com.service.ServiceImpl
    private static final String METHOD_NAME = "getEncodedMap";
    private static final String SOAP_ACTION = "https://192.168.0.32:8080/STM-MapProvider/MapProviderService";
    private static final String URL = "https://192.168.0.32:8080/STM-MapProvider/MapProviderService/getEncodedMap";

    private String webResponse = "";
    private Handler handler = new Handler();
    private Thread thread;


    public void startWebAccess(String a) {
        final String aa = a;
        thread = new Thread() {
            public void run() {
                try {
                    Log.d("Req value0R", "Starting...");// log.d is used for
                    // debug
                    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

                    PropertyInfo propInfoArg0 = new PropertyInfo();
                    propInfoArg0.setName("lat0");
                    propInfoArg0.setType(Double.class);
                    propInfoArg0.setValue(10D);
                    request.addProperty(propInfoArg0);

                    PropertyInfo propInfoArg1 = new PropertyInfo();
                    propInfoArg1.setName("long0");
                    propInfoArg1.setType(Double.class);
                    propInfoArg1.setValue(20D);
                    request.addProperty(propInfoArg1);

                    PropertyInfo propInfoArg2 = new PropertyInfo();
                    propInfoArg2.setName("lat1");
                    propInfoArg2.setType(Double.class);
                    propInfoArg2.setValue(30D);
                    request.addProperty(propInfoArg2);

                    PropertyInfo propInfoArg3 = new PropertyInfo();
                    propInfoArg3.setName("long1");
                    propInfoArg3.setType(Double.class);
                    propInfoArg3.setValue(40D);
                    request.addProperty(propInfoArg3);


                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    //envelope.dotNet = true;
                    envelope.setOutputSoapObject(request);

                    envelope.dotNet = true;
                    envelope.implicitTypes = true;
                    envelope.encodingStyle = SoapSerializationEnvelope.XSD;
                    MarshalDouble md = new MarshalDouble();
                    md.register(envelope);

                    HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
                    //androidHttpTransport.debug = true;
                    androidHttpTransport.call(SOAP_ACTION, envelope);

                    SoapObject objectResult = (SoapObject) envelope.bodyIn;
                    webResponse = objectResult.toString();
                    System.out.println("response: " + webResponse);

                } catch (SoapFault sp) {

                    sp.getMessage();
                    System.out.println("error = " + sp.getMessage());

                } catch (Exception e) {
                    System.out.println("problem8");
                    e.printStackTrace();

                    webResponse = "Connection/Internet problem";
                }

            }
        };

        thread.start();
    }
}
