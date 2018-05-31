package com.example.lenovo.scetnotifier;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Favourite extends Fragment {


    Fragment fragment;
    FragmentManager fragmentManager;

    private static final String SOAP_ACTION = "http://tempuri.org/listfav";
    private static final String METHOD_NAME = "listfav";
    private static final String NAMESPACE = "http://tempuri.org/";
    private static String urll = "";
    private static CustomeAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_favourite, container, false);
        urll = v.getResources().getString(R.string.url);

        SharedPreferences sharedPref = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
        final String StudentID = sharedPref.getString("StudentID", null);

        new CallService().execute(StudentID);

        fragment = null;

        return v;
    }

    public void setFragmentManager(int ID, String iden) {
        if (fragment != null) {
            fragmentManager = getActivity().getSupportFragmentManager();
            Bundle b = new Bundle();
            if(iden.equals("event")) {
                b.putString("EventID", String.valueOf(ID));
            }

            if(iden.equals("workshop")) {
                b.putString("workshopID", String.valueOf(ID));
            }

            if(iden.equals("job")) {
                b.putString("jobID", String.valueOf(ID));
            }

            fragment.setArguments(b);
            fragmentManager.beginTransaction().replace(R.id.grid, fragment).commit();
        }

    }

    public ArrayList<DataModel> getList(SoapObject XmlResult) {

        ArrayList<DataModel> dmArray = new ArrayList<DataModel>();
        SoapObject table = null;


        if (XmlResult.getPropertyCount() > 0) {
            for (int i = 0; i < XmlResult.getPropertyCount(); i++) {
                table = (SoapObject) XmlResult.getProperty(i);
                DataModel dm = new DataModel();
                dm.setFavTitle(table.getProperty("Title").toString());

                dm.setFavDate("Date:" + table.getProperty("Date").toString());

                dm.setFavImage(table.getProperty("Image").toString());

                dm.setFavID(table.getProperty("ID").toString());

                dm.setFaviden(table.getProperty("Identifier").toString());


                dmArray.add(dm);

            }

        }


        return dmArray;
    }

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
                        inflate(R.layout.favlist, parent, false);

                holder = new Holder();
                holder.FavTitle = (TextView) convertView.findViewById(R.id.favtitle);
                holder.FavDate = (TextView) convertView.findViewById(R.id.favdate);
                holder.ID = (TextView) convertView.findViewById(R.id.fav_ID);
                holder.iden = (TextView) convertView.findViewById(R.id.identifier);

                holder.FavImage = (ImageView) convertView.findViewById(R.id.Fav_image);
                holder.imgbtnfav = (ImageButton) convertView.findViewById(R.id.imgbtnfav);

                convertView.setTag(holder);

            } else {
                holder = (Holder) convertView.getTag();
            }
            d = dm.get(position);
            holder.FavTitle.setText(d.getFavTitle());
            holder.FavDate.setText(d.getFavDate());
            holder.ID.setText(d.getFavID());
            holder.iden.setText(d.getFaviden());
            if (holder.FavImage != null) {

                byte[] decodedstring= Base64.decode(d.getFavImage(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedstring,0,decodedstring.length);
                //final Bitmap circularBitmap = imageconverter.getRoundedCornerBitmap(decodedByte,65);
                holder.FavImage.setImageBitmap(decodedByte);
            }


           /* new Thread(new Runnable() {
                @Override
                public void run() {
                    String imgURL = dm.get(position).getFavImage();

                    final Bitmap bitImage = getBitmapFromURL(imgURL);

                    holder.FavImage.post(new Runnable() {
                        @Override
                        public void run() {
                            holder.FavImage.setImageBitmap(bitImage);
                        }
                    });
                }
            }).start();
*/
            holder.imgbtnfav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (holder.iden.getText().toString().equals("event")) {
                        fragment = new DetailEvent();
                    }
                    if (holder.iden.getText().toString().equals("workshop")) {
                        fragment = new DetailWorkshop();
                    }
                    if (holder.iden.getText().toString().equals("job")) {
                        fragment = new DetailJob();
                    }
                    setFragmentManager(Integer.parseInt(holder.ID.getText().toString()), holder.iden.getText().toString());
                }
            });
            return convertView;
        }


        public class Holder {
            TextView FavTitle;
            TextView FavDate;
            ImageView FavImage;
            TextView ID;
            TextView iden;
            ImageButton imgbtnfav;

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
        final ListView lv1 = (ListView) getActivity().findViewById(R.id.listfav);
        lv1.setAdapter(new CustomeAdapter(listOfData, this.getContext()));
    }


}
