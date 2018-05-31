package com.example.lenovo.scetnotifier;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
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
import android.view.animation.AlphaAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Notification extends Fragment {

    Fragment fragment;
    FragmentManager fragmentManager;

    //step-2
    private static final String SOAP_ACTION = "http://tempuri.org/Notification";
    private static final String METHOD_NAME = "Notification";
    private static final String NAMESPACE = "http://tempuri.org/";
    private static String urll = "";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_notification, container, false);
        urll = v.getResources().getString(R.string.url);
        new CallService().execute();


        fragment = null;
        return v;
    }


    public ArrayList<DataModel> getList(SoapObject XmlResult) {

        ArrayList<DataModel> dmArray = new ArrayList<DataModel>();
        SoapObject table = null;


        if (XmlResult.getPropertyCount() > 0) {
            for (int i = 0; i < XmlResult.getPropertyCount(); i++) {
                table = (SoapObject) XmlResult.getProperty(i);
                DataModel dm = new DataModel();
                dm.setnotiID(Integer.parseInt(table.getProperty("ID").toString()));

                dm.setnoti(table.getProperty("NotificationTitle").toString());

                dm.setnotiiden(table.getProperty("Identifier").toString());


                dm.setnotiday(table.getProperty("Day").toString());

                dm.setnotimonth(table.getProperty("Month").toString());

                dm.setnotiyear(table.getProperty("Year").toString());

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
                        inflate(R.layout.notificationlist, parent, false);

                holder = new Holder();
                holder.notiID = (TextView) convertView.findViewById(R.id.txtnotiID);
                holder.iden = (TextView) convertView.findViewById(R.id.notiiden);
                holder.notification = (TextView) convertView.findViewById(R.id.txtnoti);
                holder.date = (TextView) convertView.findViewById(R.id.txtdate);
                holder.noti_image = (ImageView) convertView.findViewById(R.id.noti_image);

                convertView.setTag(holder);

            } else {
                holder = (Holder) convertView.getTag();
            }
            d = dm.get(position);
            holder.notiID.setText(String.valueOf(d.getnotiID()));
            holder.iden.setText(d.getnotiiden());
            holder.notification.setText(d.getnoti());
            holder.date.setText(d.getnotimonth() + " " + d.getnotiday() + ", " + d.getnotiyear());
            if(d.getnotiiden().equals("event")) {


                    Bitmap imgBitMap = BitmapFactory.decodeResource(getResources(), R.drawable.notieve);
                    holder.noti_image.setImageBitmap(imgBitMap);

            }

            if(d.getnotiiden().equals("workshop")) {

                    Bitmap imgBitMap = BitmapFactory.decodeResource(getResources(), R.drawable.notiwork);
                    holder.noti_image.setImageBitmap(imgBitMap);

            }

            if(d.getnotiiden().equals("job")) {

                    Bitmap imgBitMap = BitmapFactory.decodeResource(getResources(), R.drawable.notijob);
                    holder.noti_image.setImageBitmap(imgBitMap);

            }

            return convertView;
        }


        public class Holder {
            TextView notiID;
            TextView iden;
            TextView notification;
            TextView date;
            ImageView noti_image;
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


    public class CallService extends AsyncTask<Integer, Void, SoapObject> {


        public ProgressDialog dialog = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait");
            this.dialog.setIcon(R.drawable.logo);
            this.dialog.show();


        }

        @Override
        protected SoapObject doInBackground(Integer... strings) {

            SoapObject documentElement = null;
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);


           /*PropertyInfo pi = new PropertyInfo();

            pi.setName("FirstName");
            pi.setValue(params[0]);
            pi.setType(String.class);
            request.addProperty(pi);*/

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
        final ListView lv1 = (ListView) getActivity().findViewById(R.id.listnoti);
        lv1.setAdapter(new CustomeAdapter(listOfData, this.getContext()));
    }

   /* class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;

        AlphaAnimation inAnimation;
        AlphaAnimation outAnimation;

       *//* FrameLayout progressBarHolder;

        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);*//*

        @Override
        protected void onPreExecute() {
           *//* inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            progressBarHolder.setAnimation(inAnimation);
            progressBarHolder.setVisibility(View.VISIBLE);*//*
        }

        public ImageDownloaderTask(ImageView imageView) {
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return downloadBitmap(params[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }

            if (imageViewReference != null) {
                ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    if (bitmap != null) {
                        imageView.setImageBitmap(bitmap);
                    } else {
                        Drawable placeholder = imageView.getContext().getResources().getDrawable(R.drawable.a);
                        imageView.setImageDrawable(placeholder);
                    }
                }
            }
           *//* outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);*//*
        }
    }

    private Bitmap downloadBitmap(String url) {
        HttpURLConnection urlConnection = null;
        try {
            URL uri = new URL(url);
            urlConnection = (HttpURLConnection) uri.openConnection();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != 200) {
                return null;
            }

            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream != null) {
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            }
        } catch (Exception e) {
            urlConnection.disconnect();
            Log.w("ImageDownloader", "Error downloading image from " + url);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }*/


}
