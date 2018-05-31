package com.example.lenovo.scetnotifier;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class MyAccount extends Fragment {


    private static final String SOAP_ACTION = "http://tempuri.org/MyAccount";
    private static final String METHOD_NAME = "MyAccount";
    private static final String NAMESPACE = "http://tempuri.org/";
    private static final String SOAP_ACTION1 = "http://tempuri.org/selectMyAccount";
    private static final String METHOD_NAME1 = "selectMyAccount";

    private static String urll = "";
    Button btnupdate;
    TextView fname, lname, pnumber;
    Fragment fragment;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_my_account, container, false);
        urll = getResources().getString(R.string.url);
        SharedPreferences sharedPref = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
        final String StudentID = sharedPref.getString("StudentID", null);

        btnupdate = (Button) v.findViewById(R.id.btnupdate);
        new callservice1().execute(StudentID);
        fragment = null;


        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fname = (TextView) getActivity().findViewById(R.id.firstname);
                lname = (TextView) getActivity().findViewById(R.id.lastname);
                pnumber = (TextView) getActivity().findViewById(R.id.phnnum);
                new callservice().execute(StudentID, fname.getText().toString(), lname.getText().toString(), pnumber.getText().toString());

            }


        });

        return v;
    }

    public class callservice extends AsyncTask<String, Void, SoapObject> {

        @Override
        protected SoapObject doInBackground(String... params) {
            SoapObject documentElement = null;
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            PropertyInfo pi = new PropertyInfo();
            pi.setName("Firstname");
            pi.setValue(params[1]);
            pi.setType(String.class);
            request.addProperty(pi);

            pi = new PropertyInfo();
            pi.setName("Lastname");
            pi.setValue(params[2]);
            pi.setType(String.class);
            request.addProperty(pi);

            pi = new PropertyInfo();
            pi.setName("Phoneno");
            pi.setValue(params[3]);
            pi.setType(String.class);
            request.addProperty(pi);

            pi = new PropertyInfo();
            pi.setName("StudentID");
            pi.setValue(params[0]);
            pi.setType(String.class);
            request.addProperty(pi);



            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.implicitTypes = true;
            envelope.setOutputSoapObject(request);

            try {
                HttpTransportSE androidHttpTransport = new HttpTransportSE(urll, 60000);
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

        protected void onPostExecute(SoapObject result) {
            afterCallReturn(result);
        }
    }

    private void afterCallReturn(SoapObject result) {
        SoapObject table = null;
        if (result.getPropertyCount() > 0) {

            table = (SoapObject) result.getProperty(0);
            String x = table.getProperty("ReturnValue").toString();
            if (x.equals("1")) {

                Toast.makeText(getActivity(), "Updated profile successfully", Toast.LENGTH_SHORT).show();


            } else {
                Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
            }

        }

    }

    public class callservice1 extends AsyncTask<String, Void, SoapObject> {

        @Override
        protected SoapObject doInBackground(String... params) {
            SoapObject documentElement = null;
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME1);

            PropertyInfo pi = new PropertyInfo();
            pi.setName("StudentID");
            pi.setValue(params[0]);
            pi.setType(String.class);
            request.addProperty(pi);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.implicitTypes = true;
            envelope.setOutputSoapObject(request);

            try {
                HttpTransportSE androidHttpTransport = new HttpTransportSE(urll, 60000);
                androidHttpTransport.debug = true;
                androidHttpTransport.call(SOAP_ACTION1, envelope);

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

        protected void onPostExecute(SoapObject result) {
            afterCallReturn1(result);
        }
    }

    private void afterCallReturn1(SoapObject result) {
        SoapObject table = null;
        if (result.getPropertyCount() > 0) {

            table = (SoapObject) result.getProperty(0);
            fname = (TextView) getActivity().findViewById(R.id.firstname);
            fname.setText("Firstname:" + table.getProperty("Firstname").toString());

            lname = (TextView) getActivity().findViewById(R.id.lastname);
            lname.setText("Lastname:" + table.getProperty("Lastname").toString());

            pnumber = (TextView) getActivity().findViewById(R.id.phnnum);
            pnumber.setText("Phonenumber:" + table.getProperty("PhoneNo").toString());

        }

    }



}
