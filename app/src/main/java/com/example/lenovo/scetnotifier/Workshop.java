package com.example.lenovo.scetnotifier;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Workshop extends Fragment {

    ListView listworkshop;
    private static CustomeAdapter adapter;
    Fragment fragment;
    FragmentManager fragmentManager;

    private static final String NAMESPACE = "http://tempuri.org/";
    private static final String METHOD_NAME = "showWorkshop";
    private static String url = "";
    private static final String SOAP_ACTION = "http://tempuri.org/showWorkshop";

/*
 step 0
*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_workshop, container, false);
        url = v.getResources().getString(R.string.url);

        setAppDirection setDirection = new setAppDirection(getActivity());

        String lastDirection = setDirection.getLastDirection().toString();
        // && lastDirection.equals("-1") == false
        if (lastDirection.equals("workshop") == false) {
            setDirection.setActivityDirection("workshop");
        }

/*
     step 4
*/
       /* listworkshop = (ListView) v.findViewById(R.id.listworkshop);
        adapter = new CustomeAdapter(getList(), v.getContext());
        listworkshop.setAdapter(adapter);*/

        new CallServices().execute("test");
        fragment = null;

        return v;
    }


    public void setFragmentManager(int workshopID) {
        if (fragment != null) {
            fragmentManager = getActivity().getSupportFragmentManager();
            Bundle b = new Bundle();
            b.putString("workshopID", String.valueOf(workshopID));
            fragment.setArguments(b);
            fragmentManager.beginTransaction().replace(R.id.grid, fragment).commit();
        }

    }

    /*
    step--1
    */
    public ArrayList<DataModel> getList(SoapObject XmlResult) {


        ArrayList<DataModel> dmArray = new ArrayList<DataModel>();
        SoapObject table = null;
      /*  DataModel dm = new DataModel();
        dm.setTitle("Application Workshop");
        dm.setDate("Date:30 Sept,2017");
        dm.setSubject("Subject:Android");
        dmArray.add(dm);

        dm = new DataModel();
        dm.setTitle("Website Development");
        dm.setDate("Date:31 Aug,2017");
        dm.setSubject("Subject:PHP,ASP.NET");
        dmArray.add(dm);

        dm = new DataModel();
        dm.setTitle("Rangoli Design ");
        dm.setDate("Date:31 Aug,2017");
        dm.setSubject("Subject:3D rangoli,Simple rangoli");
        dmArray.add(dm);

        dm = new DataModel();
        dm.setTitle("Website Development");
        dm.setDate("Date:31 Aug,2017");
        dm.setSubject("Subject:PHP,ASP.NET");
        dmArray.add(dm);

        dm = new DataModel();
        dm.setTitle("Website Development");
        dm.setDate("Date:31 Aug,2017");
        dm.setSubject("Subject:MVC");
        dmArray.add(dm);
*/

        if (XmlResult.getPropertyCount() > 0) {
            for (int i = 0; i < XmlResult.getPropertyCount(); i++) {

                table = (SoapObject) XmlResult.getProperty(i);

                DataModel dm = new DataModel();
                dm.setworkshopID(Integer.parseInt(table.getProperty("WorkshopID").toString()));

                dm.setTitle(table.getProperty("WorkshopName").toString());
/*
                dm.setSubject(table.getProperty("WorkshopTopic").toString());
*/
                dm.setDate("Date:" + table.getProperty("WorkshopDate").toString());

                dm.setimage(table.getProperty("WorkshopImage").toString());

                dmArray.add(dm);

            }
        }


        return dmArray;
    }


    /*
        step---2
    */
    public class CustomeAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<DataModel> dm;

        public CustomeAdapter(ArrayList<DataModel> data, Context context) {
            this.dm = data;
            this.context = context;

        }

        @Override
        public int getCount() {
            return dm.size();
        }

        @Override
        public Object getItem(int position) {
            return dm.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final Holder holder;
            DataModel d = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).
                        inflate(R.layout.workshoplist, parent, false);

                holder = new Holder();
                holder.txtTitle = (TextView) convertView.findViewById(R.id.txtViewWorkshopTitle);
                holder.txtDate = (TextView) convertView.findViewById(R.id.txtViewWorkshopDate);
/*
                holder.txtSubject = (TextView) convertView.findViewById(R.id.txtViewWorkshopSubject);
*/
                holder.btnviewworkshop = (ImageButton) convertView.findViewById(R.id.btnviewworkshop);
                holder.WorkshopID = (TextView) convertView.findViewById(R.id.workshopID);
                holder.workshopimg = (ImageView) convertView.findViewById(R.id.workshop_image);
                convertView.setTag(holder);

            } else {
                holder = (Holder) convertView.getTag();
            }
            d = dm.get(position);
            holder.txtTitle.setText(d.getTitle());
            holder.txtDate.setText(d.getDate());
            holder.WorkshopID.setText(String.valueOf(d.getworkshopID()));

            if (holder.workshopimg != null) {

                byte[] decodedstring= Base64.decode(d.getimage(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedstring,0,decodedstring.length);
                //final Bitmap circularBitmap = imageconverter.getRoundedCornerBitmap(decodedByte,65);
                holder.workshopimg.setImageBitmap(decodedByte);
            }

/*
            holder.txtSubject.setText(d.getSubject());
*/

         /*   new Thread(new Runnable() {
                @Override
                public void run() {
                    String imgURL = dm.get(position).getimage();

                    final Bitmap bitImage = getBitmapFromURL(imgURL);

                    holder.workshopimg.post(new Runnable() {
                        @Override
                        public void run() {
                            holder.workshopimg.setImageBitmap(bitImage);
                        }
                    });
               }
            }).start();*/
            holder.btnviewworkshop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragment = new DetailWorkshop();
                    setFragmentManager(Integer.parseInt(holder.WorkshopID.getText().toString()));
                }
            });

            return convertView;
        }


        /*
            step 3
        */
        public class Holder {
            TextView txtTitle;
            TextView WorkshopID;
            TextView txtDate;

            /*
                        TextView txtSubject;
            */
            ImageButton btnviewworkshop;
            ImageView workshopimg;
        }
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


    public class CallServices extends AsyncTask<String, Void, SoapObject> {

        private ProgressDialog dialog = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }

        @Override
        protected SoapObject doInBackground(String... strings) {

            SoapObject documentElement = null;

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

           /* PropertyInfo pi = new PropertyInfo();
            pi.setName("FirstName");
            pi.setValue(params[0]);
            pi.setType(String.class);
            request.addProperty(pi);*/

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
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

        }
    }

    public void afterCallReturn(SoapObject result) {
        ArrayList<DataModel> listOfData = getList(result);
        final GridView lv1 = (GridView) getActivity().findViewById(R.id.listworkshop);
        lv1.setAdapter(new CustomeAdapter(listOfData, this.getContext()));
    }

}




/*
flow for listview
workshop fragment in java file
xml file for design custom listview
java class for setting property datamodel
java file of workshop
*/
