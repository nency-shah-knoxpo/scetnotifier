package com.example.lenovo.scetnotifier;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class login extends AppCompatActivity {

    Button btnLogin;
    TextView txtlinkreg, txtEmail, txtpwd;


    private static final String SOAP_ACTION = "http://tempuri.org/Login";
    private static final String METHOD_NAME = "Login";
    private static final String NAMESPACE = "http://tempuri.org/";
    private static String urll = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        urll = getResources().getString(R.string.url);

        btnLogin = (Button) findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                txtEmail = (TextView) findViewById(R.id.loginemail);

                txtpwd = (TextView) findViewById(R.id.loginpwd);

                String x = txtEmail.getText().toString();
                String y = txtpwd.getText().toString();

                new CallServices().execute(x, y);


                /*Intent i = new Intent(v.getContext(), drawer.class);
                startActivity(i);*/
            }
        });

        txtlinkreg = (TextView) findViewById(R.id.txtlinkreg);
        txtlinkreg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                Intent intent = new Intent(v.getContext(), Regestration.class);
                startActivity(intent);
            }
        });

        SharedPreferences sharedPref = this.getSharedPreferences("Login", Context.MODE_PRIVATE);
        String isLoggedIn = sharedPref.getString("StudentID", null);
        if (isLoggedIn != null) {
            if (isLoggedIn.equals("") == false) {
                Intent i = new Intent(this, drawer.class);
                startActivity(i);
            }

        }
    }

    public class CallServices extends AsyncTask<String, Void, SoapObject> {

        @Override
        protected SoapObject doInBackground(String... params) {

            SoapObject documentElement = null;

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            PropertyInfo pi = new PropertyInfo();
            pi.setName("EmailID");//EmailID is para to webservice same name "EmailID" as given in webservice
            pi.setValue(params[0]);
            pi.setType(String.class);
            request.addProperty(pi);

            pi = new PropertyInfo();
            pi.setName("Password");
            pi.setValue(params[1]);
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
            String sID = table.getProperty("StudentID").toString();
            String x = table.getProperty("ReturnValue").toString();
            if (x.equals("1")) {

                SharedPreferences sharedPref = this.getSharedPreferences("Login", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("StudentID", sID);
                editor.commit();


                Toast.makeText(login.this, "Login successfully", Toast.LENGTH_SHORT).show();
/*
                Intent i = new Intent(this, drawer.class);
*/

                Intent i = new Intent(this, splash.class);

                startActivity(i);


            } else {
                Toast.makeText(login.this, "login Failed", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(this, Regestration.class);
                startActivity(i);
            }

        }


    }

}
