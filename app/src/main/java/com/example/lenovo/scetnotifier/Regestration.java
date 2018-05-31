package com.example.lenovo.scetnotifier;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Regestration extends AppCompatActivity {

    Spinner spinnerDept;
    Spinner spinnerYear;
    Button btnreg;
    TextView txtFname, txtLname, txtEmail, txtpwd, txtpno, txtcgpa, spnbranch, spnyear;
    ImageView imgstu;


    private static final String SOAP_ACTION = "http://tempuri.org/InsertStudent";
    private static final String METHOD_NAME = "InsertStudent";
    private static final String NAMESPACE = "http://tempuri.org/";
    private static String urll = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        urll = getResources().getString(R.string.url);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regestration);

        spinnerDept = (Spinner) findViewById(R.id.spinnerDept);
        ArrayAdapter<CharSequence> ad = ArrayAdapter.createFromResource(this, R.array.arrDept, android.R.layout.simple_spinner_item);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDept.setAdapter(ad);


        spinnerYear = (Spinner) findViewById(R.id.spinnerYear);
        ArrayAdapter<CharSequence> year = ArrayAdapter.createFromResource(this, R.array.arrYear, android.R.layout.simple_spinner_item);
        year.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYear.setAdapter(year);

        btnreg = (Button) findViewById(R.id.btnreg);
        btnreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtFname = (TextView) findViewById(R.id.FirstName);
                txtLname = (TextView) findViewById(R.id.LastName);

                txtEmail = (TextView) findViewById(R.id.EmailID);

                txtpwd = (TextView) findViewById(R.id.pwd);


                txtpno = (TextView) findViewById(R.id.Pno);

                txtcgpa = (TextView) findViewById(R.id.Cgpa);

                String branch = spinnerDept.getSelectedItem().toString();

                String year = spinnerYear.getSelectedItem().toString();


                new CallService().execute(txtFname.getText().toString(), txtLname.getText().toString(), txtEmail.getText().toString(), txtpwd.getText().toString(), txtpno.getText().toString(), txtcgpa.getText().toString(), branch, year);



                /*Intent i = new Intent(v.getContext(), login.class);
                startActivity(i);*/
            }
        });

    }



    public class CallService extends AsyncTask<String, Void, SoapObject> {

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
            pi.setName("LasttName");
            pi.setValue(params[1]);
            pi.setType(String.class);
            request.addProperty(pi);


            pi = new PropertyInfo();
            pi.setName("Emailid");
            pi.setValue(params[2]);
            pi.setType(String.class);
            request.addProperty(pi);

            pi = new PropertyInfo();
            pi.setName("Password");
            pi.setValue(params[3]);
            pi.setType(String.class);
            request.addProperty(pi);

            pi = new PropertyInfo();
            pi.setName("PhoneNo");
            pi.setValue(params[4]);
            pi.setType(String.class);
            request.addProperty(pi);

            pi = new PropertyInfo();
            pi.setName("CGPA");
            pi.setValue(params[5]);
            pi.setType(Integer.class);
            request.addProperty(pi);


            pi = new PropertyInfo();
            pi.setName("Branch");
            pi.setValue(params[6]);
            pi.setType(String.class);
            request.addProperty(pi);


            pi = new PropertyInfo();
            pi.setName("Year");
            pi.setValue(params[7]);
            pi.setType(Integer.class);
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

                Toast.makeText(Regestration.this, "Regestered successfully", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(this, login.class);
                startActivity(i);


            } else {
                Toast.makeText(Regestration.this, "Failed", Toast.LENGTH_SHORT).show();
            }

        }


    }


}
