package com.example.stm_client;

import android.os.Handler;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class TestRequest {
    private static final String NAMESPACE = "http://tempuri.org/"; // com.service.ServiceImpl
    private static final String URL = "http://commodities.karvy.com/services/NetPositionReport.asmx";
    private static final String METHOD_NAME = "NetPositionReport";
    private static final String SOAP_ACTION = "http://tempuri.org/NetPositionReport";
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

                    request.addProperty("ClientCode", "64396");
                    request.addProperty("key", "Om$@!#@M^#R");

                    Log.d("Req value1", request.toString());

                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                            SoapEnvelope.VER11);
                    envelope.dotNet = true;
                    envelope.setOutputSoapObject(request);

                    HttpTransportSE androidHttpTransport = new HttpTransportSE(
                            URL);
                    androidHttpTransport.debug = true;
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
