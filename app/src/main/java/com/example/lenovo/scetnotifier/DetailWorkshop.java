package com.example.lenovo.scetnotifier;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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

public class DetailWorkshop extends Fragment {


    TextView txtWorktitle, txtworkdate, txtworkdetail,txttime,txtvenue,txtregworkid;
    ImageView workimg;
    Fragment fragment;
    Button btnwork,btnworkfav;
    private static final String SOAP_ACTION = "http://tempuri.org/DetailWorkshop";
    private static final String METHOD_NAME = "DetailWorkshop";
    private static final String NAMESPACE = "http://tempuri.org/";
    private static String urll = "";

    private static final String SOAP_ACTION1 = "http://tempuri.org/ApplyWorkshop";
    private static final String METHOD_NAME1= "ApplyWorkshop";

    private static final String SOAP_ACTION2 = "http://tempuri.org/Favourite";
    private static final String METHOD_NAME2 = "Favourite";


    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_detail_workshop, container, false);
        urll = v.getResources().getString(R.string.url);

        setAppDirection setDirection = new setAppDirection(getActivity());

        String lastDirection = setDirection.getLastDirection().toString();
        // && lastDirection.equals("-1") == false
        if (lastDirection.equals("detailworkshop") == false) {
            setDirection.setActivityDirection("detailworkshop");
        }

        String workshopID = getArguments().getString("workshopID").toString();

        SharedPreferences sharedPref = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
        final String StudentID = sharedPref.getString("StudentID",null);

        btnwork=(Button)v.findViewById(R.id.btnregwork);
        txtregworkid=(TextView)v.findViewById(R.id.txtregwork);

        txtregworkid.setText(workshopID);



        btnwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new CallService1().execute(StudentID,txtregworkid.getText().toString());


            }
        });

        btnworkfav = (Button)v.findViewById(R.id.btnworkfav);
        btnworkfav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String x = "workshop";
                new Favourite().execute(StudentID,txtregworkid.getText().toString(),x);

            }
        });


        new CallService().execute(Integer.parseInt(workshopID.toString()));



        fragment = null;

        return v;

    }

    public Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception", e.getMessage());
            return null;
        }
    }


    public class CallService extends AsyncTask<Integer,Void,SoapObject>
    {

        private ProgressDialog dialog = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }


        @Override
        protected SoapObject doInBackground(Integer... params) {
            SoapObject documentElement = null;
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            PropertyInfo pi = new PropertyInfo();
            pi.setName("workshopID");
            pi.setValue(params[0]);
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
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

        }

    }
    /*TextView txtWorktitle, txtworkdate, txtworkdetail,txttime,txtvenue;
    ImageView workimg;*/
    private void afterCallReturn(SoapObject result) {
        SoapObject table = null;
        if (result.getPropertyCount() > 0) {
            table = (SoapObject) result.getProperty(0);
            txtWorktitle = (TextView) getActivity().findViewById(R.id.workname);
            txtWorktitle.setText(table.getProperty("WorkshopName").toString());

            txtworkdate = (TextView) getActivity().findViewById(R.id.workdate);
            txtworkdate.setText(table.getProperty("WorkshopDate").toString());

            txtworkdetail = (TextView) getActivity().findViewById(R.id.workdetails);
            txtworkdetail.setText(table.getProperty("WorkshopDetails").toString());

            txttime = (TextView) getActivity().findViewById(R.id.worktime);
            txttime.setText(table.getProperty("WorkshopTime").toString());


            txtvenue = (TextView) getActivity().findViewById(R.id.workvenue);
            txtvenue.setText(table.getProperty("WorkshopVenue").toString());

            final String imgUrl = table.getProperty("WorkshopImage").toString();
            workimg = (ImageView) getActivity().findViewById(R.id.workimage);
            if (workimg != null) {

                byte[] decodedstring= Base64.decode(imgUrl, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedstring,0,decodedstring.length);
               // final Bitmap circularBitmap = imageconverter.getRoundedCornerBitmap(decodedByte,65);
                workimg.setImageBitmap(decodedByte);
            }


        }


    }



    public class CallService1 extends AsyncTask<String,Void,SoapObject>
    {

        @Override
        protected SoapObject doInBackground(String... params) {
            SoapObject documentElement = null;
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME1);

            PropertyInfo pi = new PropertyInfo();
            pi.setName("StudentID");
            pi.setValue(params[0]);
            pi.setType(String.class);
            request.addProperty(pi);

            pi = new PropertyInfo();
            pi.setName("workshopID");
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
            String x = table.getProperty("ReturnValue").toString();
            if (x.equals("1")) {




                Toast.makeText(this.getActivity(), "Regestered to event successfully", Toast.LENGTH_SHORT).show();



            } else {

                Toast.makeText(this.getActivity(), "can not Regestered to event", Toast.LENGTH_SHORT).show();

            }


        }


    }

    public class Favourite extends AsyncTask<String, Void, SoapObject> {
        @Override
        protected SoapObject doInBackground(String... params) {
            SoapObject documentElement = null;
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME2);

            PropertyInfo pi = new PropertyInfo();
            pi.setName("StudentID");
            pi.setValue(params[0]);
            pi.setType(String.class);
            request.addProperty(pi);

            pi = new PropertyInfo();
            pi.setName("ID");
            pi.setValue(params[1]);
            pi.setType(String.class);
            request.addProperty(pi);

            pi = new PropertyInfo();
            pi.setName("str");
            pi.setValue(params[2]);
            pi.setType(String.class);
            request.addProperty(pi);




            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.implicitTypes = true;
            envelope.setOutputSoapObject(request);
            try {
                HttpTransportSE androidHttpTransport = new HttpTransportSE(urll, 60000);
                androidHttpTransport.debug = true;
                androidHttpTransport.call(SOAP_ACTION2, envelope);

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
            afterCallReturn2(result);
        }


    }

    private void afterCallReturn2(SoapObject result) {
        SoapObject table = null;
        if (result.getPropertyCount() > 0) {
            table = (SoapObject) result.getProperty(0);
            String x = table.getProperty("ReturnValue").toString();
            if (x.equals("1")) {




                Toast.makeText(this.getActivity(), "workshop Added to favourite ", Toast.LENGTH_SHORT).show();



            } else {

                Toast.makeText(this.getActivity(), "workshop can not Added to favourite", Toast.LENGTH_SHORT).show();

            }


        }

        /*ArrayList<DataModel> listOfData = getList(result);
        final ListView lv1 = (ListView) getActivity().findViewById(R.id.listjob);
        lv1.setAdapter(new CustomeAdapter(listOfData, this.getContext()));*/
    }


}
