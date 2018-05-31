package com.example.lenovo.scetnotifier;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
//check order of fav(parameter)
public class DetailEvent extends Fragment {
    TextView txtEveDetail, EveName, EventTime, EveDate, EveVenue;
    ImageView EveImage;
    Fragment fragment;
    Button btnreg,btnfaveve;
    TextView detaileventid;

    //step 3 variables declaration and definition
    private static final String SOAP_ACTION = "http://tempuri.org/DetailEvent";
    private static final String METHOD_NAME = "DetailEvent";
    private static final String NAMESPACE = "http://tempuri.org/";
    private static String urll = "";


    private static final String SOAP_ACTION1 = "http://tempuri.org/ApplyEvent";
    private static final String METHOD_NAME1 = "ApplyEvent";


    private static final String SOAP_ACTION2 = "http://tempuri.org/Favourite";
    private static final String METHOD_NAME2 = "Favourite";


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_detail_event, container, false);
        urll = v.getResources().getString(R.string.url);


        setAppDirection setDirection = new setAppDirection(getActivity());

        String lastDirection = setDirection.getLastDirection().toString();
        // && lastDirection.equals("-1") == false
        if (lastDirection.equals("detailevent") == false) {
            setDirection.setActivityDirection("detailevent");
        }

        SharedPreferences sharedPref = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
        final String StudentID = sharedPref.getString("StudentID",null);

        final String EventID = getArguments().getString("EventID").toString();
        detaileventid = (TextView)v.findViewById(R.id.detaileventid) ;
        detaileventid.setText(EventID);




       /* txtEveDetail = (TextView) v.findViewById(R.id.EveDetails);
        String formattedText = "IT Department organize Converse under kshitij-Technical Festival." +
                "In Converse 2k17,There are 5 Events.<br />" +
                "        1.Technical Presentation <br />" +
                "        2.Relay Coding <br />" +
                "        3.Image IT <br />" +
                "        4.PY-IT <br />" +
                "        5.UI-Mockup<br />" +
                "Do participate in all the events.";
// or getString(R.string.htmlFormattedText);
        txtEveDetail.setText(Html.fromHtml(formattedText));
*/

        btnreg = (Button)v.findViewById(R.id.btnevereg);

        btnreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new CallService1().execute(StudentID,detaileventid.getText().toString());


            }
        });

        btnfaveve = (Button)v.findViewById(R.id.btnevefav) ;
        btnfaveve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String x = "event";
                new Favourite().execute(StudentID,detaileventid.getText().toString(),x);

            }
        });








        //step4 Call The Class
        new CallService().execute(Integer.parseInt(EventID.toString()));

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


    //Step 1  Asynctask begin

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
            pi.setName("eventID");
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


    //Asynctask over

    //step 2 Function aftercall return define

    private void afterCallReturn(SoapObject result) {
        SoapObject table = null;
        if (result.getPropertyCount() > 0) {
            table = (SoapObject) result.getProperty(0);
            EveName = (TextView) getActivity().findViewById(R.id.EveName);
            EveName.setText(table.getProperty("EventName").toString());

            txtEveDetail = (TextView) getActivity().findViewById(R.id.EveDetails);
            txtEveDetail.setText(table.getProperty("EventDetails").toString());

            EveDate = (TextView) getActivity().findViewById(R.id.EveDate);
            EveDate.setText(table.getProperty("EventDate").toString());

            EveVenue = (TextView) getActivity().findViewById(R.id.EveVenue);
            EveVenue.setText(table.getProperty("EventVenue").toString());


            EventTime = (TextView) getActivity().findViewById(R.id.EveTime);
            EventTime.setText(table.getProperty("EventTime").toString());

            final String imgUrl = table.getProperty("EventImage").toString();
            EveImage = (ImageView) getActivity().findViewById(R.id.Event_image_detail);
            /*new Thread(new Runnable() {
                @Override
                public void run() {
                    final Bitmap bitImage = getBitmapFromURL(imgUrl);

                    EveImage.post(new Runnable() {
                        @Override
                        public void run() {
                            EveImage.setImageBitmap(bitImage);
                        }
                    });
                }
            }).start();
*/
            if (EveImage != null) {

                byte[] decodedstring= Base64.decode(imgUrl, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedstring,0,decodedstring.length);
               // final Bitmap circularBitmap = imageconverter.getRoundedCornerBitmap(decodedByte,65);
                EveImage.setImageBitmap(decodedByte);
            }



        }

        /*ArrayList<DataModel> listOfData = getList(result);
        final ListView lv1 = (ListView) getActivity().findViewById(R.id.listjob);
        lv1.setAdapter(new CustomeAdapter(listOfData, this.getContext()));*/
    }





    //Asynctaskclass for btn click event


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
            pi.setName("eventID");
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

        /*ArrayList<DataModel> listOfData = getList(result);
        final ListView lv1 = (ListView) getActivity().findViewById(R.id.listjob);
        lv1.setAdapter(new CustomeAdapter(listOfData, this.getContext()));*/
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




                Toast.makeText(this.getActivity(), "Event Added to favourite ", Toast.LENGTH_SHORT).show();



            } else {

                Toast.makeText(this.getActivity(), "event can not Added to favourite", Toast.LENGTH_SHORT).show();

            }


        }

        /*ArrayList<DataModel> listOfData = getList(result);
        final ListView lv1 = (ListView) getActivity().findViewById(R.id.listjob);
        lv1.setAdapter(new CustomeAdapter(listOfData, this.getContext()));*/
    }


}
