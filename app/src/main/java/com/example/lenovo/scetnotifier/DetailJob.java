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

import javax.crypto.Cipher;

public class DetailJob extends Fragment {


    TextView txtcomname, txtbatch, txtbranch, JobType, JobSalary, JobComLink, criteria, position, skills, Location,hiddenid;
    ImageView comimg;
    Fragment fragment;
Button btn,btnjobfav;
    private static final String SOAP_ACTION = "http://tempuri.org/DetailJob";
    private static final String METHOD_NAME = "DetailJob";
    private static final String NAMESPACE = "http://tempuri.org/";
    private static String urll = "";

    private static final String SOAP_ACTION1 = "http://tempuri.org/ApplyJob";
    private static final String METHOD_NAME1 = "ApplyJob";

    private static final String SOAP_ACTION2 = "http://tempuri.org/Favourite";
    private static final String METHOD_NAME2 = "Favourite";




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_detail_job, container, false);
        String jobID = getArguments().getString("jobID").toString();
        urll = v.getResources().getString(R.string.url);

        setAppDirection setDirection = new setAppDirection(getActivity());

        String lastDirection = setDirection.getLastDirection().toString();
        // && lastDirection.equals("-1") == false
        if (lastDirection.equals("detailjob") == false) {
            setDirection.setActivityDirection("detailjob");
        }

        hiddenid = (TextView)v.findViewById(R.id.detailjobid) ;
        hiddenid.setText(jobID);
        fragment = null;
        SharedPreferences sharedPref = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
        final String StudentID = sharedPref.getString("StudentID",null);
        btn = (Button)v.findViewById(R.id.btnjobreg);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new CallService1().execute(StudentID,hiddenid.getText().toString());


            }
        });

        btnjobfav = (Button)v.findViewById(R.id.btnjobfav) ;
        btnjobfav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String x = "job";
                new Favourite().execute(StudentID,hiddenid.getText().toString(),x);

            }
        });


        new CallService().execute(Integer.parseInt(jobID.toString()));

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


    public class CallService extends AsyncTask<Integer, Void, SoapObject> {

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
            pi.setName("jobID");
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
    /*TextView txtcomname, txtbatch, txtbranch, JobType, JobSalary,JobComLink,criteria,position,skills,Location;
    ImageView comimg;
*/

    private void afterCallReturn(SoapObject result) {
        SoapObject table = null;
        if (result.getPropertyCount() > 0) {
            try {
                table = (SoapObject) result.getProperty(0);
                txtcomname = (TextView) getActivity().findViewById(R.id.txtcomname);
                txtcomname.setText(table.getProperty("CompanyName").toString());

                txtbatch = (TextView) getActivity().findViewById(R.id.txtbatch);
                txtbatch.setText(table.getProperty("ComBatch").toString());

                txtbranch = (TextView) getActivity().findViewById(R.id.txtbranch);
                txtbranch.setText(table.getProperty("ComBranch").toString());

                JobType = (TextView) getActivity().findViewById(R.id.JobType);
                JobType.setText(table.getProperty("JobType").toString());


                JobSalary = (TextView) getActivity().findViewById(R.id.JobSalary);
                JobSalary.setText(table.getProperty("Salary").toString());


                JobComLink = (TextView) getActivity().findViewById(R.id.JobComLink);
                JobComLink.setText(table.getProperty("WebLink").toString());

                criteria = (TextView) getActivity().findViewById(R.id.criteria);
                criteria.setText(table.getProperty("EligibiltyCriteria").toString());

                position = (TextView) getActivity().findViewById(R.id.position);
                position.setText(table.getProperty("Position").toString());

                skills = (TextView) getActivity().findViewById(R.id.Skills);
                skills.setText(table.getProperty("Skills").toString());

                Location = (TextView) getActivity().findViewById(R.id.Location);
                Location.setText(table.getProperty("Location").toString());

                final String imgUrl = table.getProperty("CompanyImage").toString();
                comimg = (ImageView) getActivity().findViewById(R.id.comimg);
                if (comimg != null) {

                    byte[] decodedstring= Base64.decode(imgUrl, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedstring,0,decodedstring.length);
                    //final Bitmap circularBitmap = imageconverter.getRoundedCornerBitmap(decodedByte,65);
                    comimg.setImageBitmap(decodedByte);
                }
               /* new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final Bitmap bitImage = getBitmapFromURL(imgUrl);

                        comimg.post(new Runnable() {
                            @Override
                            public void run() {
                                comimg.setImageBitmap(bitImage);
                            }
                        });
                    }
                }).start();
*/

            } catch (Exception e) {
                Log.e("Error", e.getMessage());
            }
        }

        /*ArrayList<DataModel> listOfData = getList(result);
        final ListView lv1 = (ListView) getActivity().findViewById(R.id.listjob);
        lv1.setAdapter(new CustomeAdapter(listOfData, this.getContext()));*/
    }



    //Asyncclass2 for btn click of reg on which applyworkshop webservice is called

    public class CallService1 extends AsyncTask<String, Void, SoapObject> {

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
            pi.setName("jobID");
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




                Toast.makeText(this.getActivity(), "Regestered for placement successfully", Toast.LENGTH_SHORT).show();



            } else {

                Toast.makeText(this.getActivity(), "can not Regestered to Placement", Toast.LENGTH_SHORT).show();

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




                Toast.makeText(this.getActivity(), "Placement Added to favourite ", Toast.LENGTH_SHORT).show();



            } else {

                Toast.makeText(this.getActivity(), "Placement can not Added to favourite", Toast.LENGTH_SHORT).show();

            }


        }

        /*ArrayList<DataModel> listOfData = getList(result);
        final ListView lv1 = (ListView) getActivity().findViewById(R.id.listjob);
        lv1.setAdapter(new CustomeAdapter(listOfData, this.getContext()));*/
    }

}
