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

public class Feedback extends Fragment {

    Fragment fragment;
    TextView fname, eid, feedback;
    Button btnfeedback;
    private static final String SOAP_ACTION = "http://tempuri.org/Feedback";
    private static final String METHOD_NAME = "Feedback";
    private static final String NAMESPACE = "http://tempuri.org/";
    private static String urll = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_feedback, container, false);

        urll = v.getResources().getString(R.string.url);


        SharedPreferences sharedPref = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
        final String StudentID = sharedPref.getString("StudentID", null);
        btnfeedback = (Button)v.findViewById(R.id.btnfeedback);
        btnfeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fname = (TextView)getActivity().findViewById(R.id.txtfeedname);
                eid = (TextView)getActivity().findViewById(R.id.txtfeedemail);
                feedback = (TextView)getActivity().findViewById(R.id.txtfeedback);

                new callservice().execute(fname.getText().toString(),eid.getText().toString(),feedback.getText().toString(),StudentID);

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
            pi.setName("FirstName");
            pi.setValue(params[0]);
            pi.setType(String.class);
            request.addProperty(pi);

            pi = new PropertyInfo();
            pi.setName("Emailid");
            pi.setValue(params[1]);
            pi.setType(String.class);
            request.addProperty(pi);

            pi = new PropertyInfo();
            pi.setName("feedback");
            pi.setValue(params[2]);
            pi.setType(String.class);
            request.addProperty(pi);

            pi = new PropertyInfo();
            pi.setName("StudentID");
            pi.setValue(params[3]);
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

                Toast.makeText(getActivity(), "Feedback registered successfully", Toast.LENGTH_SHORT).show();



            } else {
                Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
            }

        }

    }

}
