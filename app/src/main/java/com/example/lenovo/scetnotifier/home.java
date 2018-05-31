package com.example.lenovo.scetnotifier;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class home extends Fragment {

    GridView grdhome;

    Fragment fragment;
    FragmentManager fragmentManager;

    private static final String SOAP_ACTION = "http://tempuri.org/listfav";
    private static final String METHOD_NAME = "listfav";
    private static final String NAMESPACE = "http://tempuri.org/";
    private static String urll = "";
    private static CustomeAdapter adapter;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_home, container, false);

        urll = v.getResources().getString(R.string.url);

        SharedPreferences sharedPref = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
        final String StudentID = sharedPref.getString("StudentID", null);

        new CallService().execute(StudentID);

        fragment = null;

        /*grdhome = (GridView)v.findViewById(R.id.grdNewsFeed);
        adapter = new CustomeAdapter(getList(), v.getContext());
        grdhome.setAdapter(adapter);*/
        return v;
    }

    public ArrayList<DataModel> getList(SoapObject XmlResult) {

        ArrayList<DataModel> dmArray = new ArrayList<DataModel>();

        /*DataModel dm = new DataModel();
        dm.setTitle("App Workshop");
        dm.setDate("Date:30 Sept,2017");
        dmArray.add(dm);

        dm = new DataModel();
        dm.setTitle("Converse 2k17");
        dm.setDate("Date:31 Aug,2017");
        dmArray.add(dm);

        dm = new DataModel();
        dm.setTitle("Supplenta");
        dm.setDate("Date:31 Aug,2017");
        dmArray.add(dm);

        dm = new DataModel();
        dm.setTitle("Thomsan");
        dm.setDate("Date:31 Aug,2017");
        dmArray.add(dm);

        return dmArray;*/
        SoapObject table = null;


        if (XmlResult.getPropertyCount() > 0) {
            for (int i = 0; i < XmlResult.getPropertyCount(); i++) {
                table = (SoapObject) XmlResult.getProperty(i);
                DataModel dm = new DataModel();
                dm.setFavTitle(table.getProperty("Title").toString());

                dm.setFavDate("Date:" + table.getProperty("Date").toString());

                dm.setFavImage(table.getProperty("Image").toString());

               // dm.setFavID(table.getProperty("ID").toString());

              //  dm.setFaviden(table.getProperty("Identifier").toString());


                dmArray.add(dm);

            }

        }


        return dmArray;



    }

    public class CustomeAdapter extends BaseAdapter{
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
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;
            DataModel d = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).
                        inflate(R.layout.homelist, parent, false);

                holder = new Holder();
                holder.txtTitle = (TextView) convertView.findViewById(R.id.HomeName);
                holder.txtDate = (TextView) convertView.findViewById(R.id.HomeDate);
                holder.imghome = (ImageView) convertView.findViewById(R.id.imghome);

                convertView.setTag(holder);

            } else {
                holder = (Holder) convertView.getTag();
            }
            d = dm.get(position);
            holder.txtTitle.setText(d.getFavTitle());
            holder.txtDate.setText(d.getFavDate());
            if (holder.imghome != null) {

                byte[] decodedstring= Base64.decode(d.getFavImage(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedstring,0,decodedstring.length);
                //final Bitmap circularBitmap = imageconverter.getRoundedCornerBitmap(decodedByte,65);
                holder.imghome.setImageBitmap(decodedByte);
            }

            return convertView;
        }

        public class Holder {
            TextView txtTitle;
            TextView txtDate;
            ImageView imghome;
        }
    }

    public class CallService extends AsyncTask<String, Void, SoapObject> {
        private ProgressDialog dialog = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }

        @Override
        protected SoapObject doInBackground(String... params) {

            SoapObject documentElement = null;
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);


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
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

        }
    }

    //step4

    private void afterCallReturn(SoapObject result) {
        ArrayList<DataModel> listOfData = getList(result);
        final GridView lv1 = (GridView) getActivity().findViewById(R.id.grdNewsFeed);
        lv1.setAdapter(new CustomeAdapter(listOfData, this.getContext()));
    }

}
