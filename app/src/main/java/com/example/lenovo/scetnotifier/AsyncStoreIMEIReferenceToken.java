package com.example.lenovo.scetnotifier;

import android.os.AsyncTask;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

/**
 * Created by LENOVO on 27-02-2018.
 */
public class AsyncStoreIMEIReferenceToken extends AsyncTask<String, Void, SoapObject> {

    private static final String SOAP_ACTION = "http://tempuri.org/StoreIMEI";
    private static final String METHOD_NAME = "StoreIMEI";
    private static final String NAMESPACE = "http://tempuri.org/";
    private static String url = "";

    @Override
    protected SoapObject doInBackground(String... params) {

        url = "http://192.168.43.148/snWebServices/SNWebService.asmx";

        SoapObject documentElement = null;
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        PropertyInfo pi = new PropertyInfo();

        pi.setName("ReferenceToken");
        pi.setValue(params[0]);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("IMEI");
        pi.setValue(params[1]);
        pi.setType(String.class);
        request.addProperty(pi);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.implicitTypes = true;
        envelope.setOutputSoapObject(request);
        try {
            HttpTransportSE androidHttpTransport = new HttpTransportSE(url, 60000);
            androidHttpTransport.debug = true;
            androidHttpTransport.call(SOAP_ACTION, envelope);

            SoapObject response = (SoapObject) envelope.getResponse();
            SoapObject diffgram = (SoapObject) response.getProperty("diffgram");
            String v = "";
            if (diffgram.getPropertyCount() > 0) {
                documentElement = (SoapObject) diffgram.getProperty("DocumentElement");
            }
        } catch (Exception E) {
            Log.e("Load", E.toString());

        }
        return documentElement;
    }

    @Override
    protected void onPostExecute(SoapObject result) {
        afterCallReturn(result);
    }

    private void afterCallReturn(SoapObject result) {

    }

}
