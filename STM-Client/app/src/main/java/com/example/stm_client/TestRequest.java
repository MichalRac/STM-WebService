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
    private static final String NAMESPACE = "MapProvider"; // com.service.ServiceImpl
    private static final String URL = "http://192.168.0.32:8080/STM-MapProvider/MapProviderService?wsdl";
    private static final String METHOD_NAME = "getEncodedMap";
    private static final String SOAP_ACTION = "http://192.168.0.32:8080/STM-MapProvider/MapProviderService/getEncodedMap";

    private String webResponse = "";
    private Handler handler = new Handler();
    private Thread thread;
    IMapProviderListener listener;

    String tempImg = "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";

    public void startWebAccess(IMapProviderListener listener, Double long0, Double lat0, Double long1, Double lat1 ) {
        thread = new Thread() {
            public void run() {
                try {
                    Log.d("Req value0R", "Starting...");// log.d is used for
                    // debug
                    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

                    PropertyInfo propInfoArg0 = new PropertyInfo();
                    propInfoArg0.setName("lat0");
                    propInfoArg0.setType(Double.class);
                    propInfoArg0.setValue(lat0);
                    request.addProperty(propInfoArg0);

                    PropertyInfo propInfoArg1 = new PropertyInfo();
                    propInfoArg1.setName("long0");
                    propInfoArg1.setType(Double.class);
                    propInfoArg1.setValue(long0);
                    request.addProperty(propInfoArg1);

                    PropertyInfo propInfoArg2 = new PropertyInfo();
                    propInfoArg2.setName("lat1");
                    propInfoArg2.setType(Double.class);
                    propInfoArg2.setValue(lat1);
                    request.addProperty(propInfoArg2);

                    PropertyInfo propInfoArg3 = new PropertyInfo();
                    propInfoArg3.setName("long1");
                    propInfoArg3.setType(Double.class);
                    propInfoArg3.setValue(long1);
                    request.addProperty(propInfoArg3);

                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    //envelope.dotNet = true;
                    envelope.setOutputSoapObject(request);

                    //envelope.dotNet = false;
                    //envelope.implicitTypes = true;
                    //envelope.encodingStyle = SoapSerializationEnvelope.XSD;
                    MarshalDouble md = new MarshalDouble();
                    md.register(envelope);

                    HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
                    //androidHttpTransport.debug = true;
                    androidHttpTransport.call(SOAP_ACTION, envelope);

                    SoapObject objectResult = (SoapObject) envelope.bodyIn;
                    webResponse = objectResult.toString();
                    System.out.println("response: " + webResponse);

                    String formattedResponse = webResponse.substring(29, webResponse.length() - 3);

                    listener.OnMapProvided(formattedResponse);

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
