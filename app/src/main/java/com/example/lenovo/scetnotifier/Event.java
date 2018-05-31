package com.example.lenovo.scetnotifier;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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

public class Event extends Fragment {



    Fragment fragment;
    FragmentManager fragmentManager;

    //step-2
    private static final String SOAP_ACTION = "http://tempuri.org/showEvent";
    private static final String METHOD_NAME = "showEvent";
    private static final String NAMESPACE = "http://tempuri.org/";
    private static String urll = "";
    private static Event.CustomeAdapter adapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_event, container, false);
        urll = v.getResources().getString(R.string.url);

    //********set app direction*******
        setAppDirection setDirection = new setAppDirection(getActivity());

        String lastDirection = setDirection.getLastDirection().toString();
        // && lastDirection.equals("-1") == false
        if (lastDirection.equals("event") == false) {
            setDirection.setActivityDirection("event");
        }


        /*listevent = (ListView) v.findViewById(R.id.listevent);
        adapter = new Event.CustomeAdapter(getList(), v.getContext());
        listevent.setAdapter(adapter);*/
//step5


        new CallService().execute("test");

        fragment = null;
        return v;
    }

    public void setFragmentManager(int EventID) {
        if (fragment != null) {
            fragmentManager = getActivity().getSupportFragmentManager();
            Bundle b = new Bundle();
            b.putString("EventID", String.valueOf(EventID));
            fragment.setArguments(b);
            fragmentManager.beginTransaction().replace(R.id.grid, fragment).commit();
        }

    }

    public ArrayList<DataModel> getList(SoapObject XmlResult) {

        ArrayList<DataModel> dmArray = new ArrayList<DataModel>();
        SoapObject table = null;

      /*  DataModel dm = new DataModel();
        dm.setEventName("Name:Converce 2k17");
        dm.setEventVenue("Venue:Dome");
        dm.setEventDate("Date:30 Sept,2017");
        dmArray.add(dm);

        dm = new DataModel();
        dm.setEventName("Name:Kshitij");
        dm.setEventVenue("Venue:Dome");
        dm.setEventDate("Date:31 Aug,2017");
        dmArray.add(dm);

        dm = new DataModel();
        dm.setEventName("Name:Photohunt");
        dm.setEventVenue("Venue:Dome");
        dm.setEventDate("Date:30 Aug,2017");
        dmArray.add(dm);

        dm = new DataModel();
        dm.setEventName("Name:PhotoWizard");
        dm.setEventVenue("Venue:Canteen");
        dm.setEventDate("Date:1 Aug,2017");
        dmArray.add(dm);

        dm = new DataModel();
        dm.setEventName("Name:PY_IT");
        dm.setEventVenue("Venue:IT-LAB 1");
        dm.setEventDate("Date:2 Sept,2017");
        dmArray.add(dm);

        dm = new DataModel();
        dm.setEventName("Name:COIN");
        dm.setEventVenue("Venue:IT-LAB 2");
        dm.setEventDate("Date:31 August,2017");
        dmArray.add(dm);

        dm = new DataModel();
        dm.setEventName("Name:Image_IT");
        dm.setEventVenue("Venue:EG Lab");
        dm.setEventDate("Date:1 nov,2017");
        dmArray.add(dm);*/

        if (XmlResult.getPropertyCount() > 0) {
            for (int i = 0; i < XmlResult.getPropertyCount(); i++) {
                table = (SoapObject) XmlResult.getProperty(i);
                DataModel dm = new DataModel();
                dm.setEventID(Integer.parseInt(table.getProperty("EventID").toString()));

                dm.setEventName("Name:" + table.getProperty("EventName").toString());

                dm.setEventDate("Date:" + table.getProperty("EventDate").toString());

                dm.setEventVenue("Venue:" + table.getProperty("EventVenue").toString());

                dm.setEventImage(table.getProperty("EventImage").toString());

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
                        inflate(R.layout.eventlist, parent, false);

                holder = new Holder();
                holder.EventName = (TextView) convertView.findViewById(R.id.txtViewEventName);
                holder.EventVenue = (TextView) convertView.findViewById(R.id.txtViewEventVenue);
                holder.EventDate = (TextView) convertView.findViewById(R.id.txtViewEventTime);
                holder.imgbtnDetail = (ImageButton) convertView.findViewById(R.id.imgbtnEvnetDetail);
                holder.EventsID = (TextView) convertView.findViewById(R.id.txtViewEvent_ID);
                holder.imgEventImage = (ImageView) convertView.findViewById(R.id.Event_image);
                convertView.setTag(holder);

            } else {
                holder = (Holder) convertView.getTag();
            }
             d = dm.get(position);
            holder.EventName.setText(d.getEventName());
            holder.EventsID.setText(String.valueOf(d.getEventID()));
            holder.EventVenue.setText(d.getEventVenue());
            holder.EventDate.setText(d.getEventDate());
            if (holder.imgEventImage != null) {

                byte[] decodedstring=Base64.decode(d.getEventImage(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedstring,0,decodedstring.length);
                //final Bitmap circularBitmap = imageconverter.getRoundedCornerBitmap(decodedByte,65);
                holder.imgEventImage.setImageBitmap(decodedByte);
            }


           /* new Thread(new Runnable() {
                @Override
                public void run() {
                    String imgURL = dm.get(position).getEventImage();

                    final Bitmap bitImage = getBitmapFromURL(imgURL);

                    holder.imgEventImage.post(new Runnable() {
                        @Override
                        public void run() {
                            holder.imgEventImage.setImageBitmap(bitImage);
                        }
                    });
                }
            }).start();
*/

            holder.imgbtnDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragment = new DetailEvent();
                    setFragmentManager(Integer.parseInt(holder.EventsID.getText().toString()));
                }
            });

            return convertView;
        }


        public class Holder {
            TextView EventName;
            TextView EventVenue;
            TextView EventDate;
            ImageButton imgbtnDetail;
            TextView EventsID;
            ImageView imgEventImage;
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
//step1 for getting dynamic record

    public class CallService extends AsyncTask<String, Void, SoapObject> {


        public ProgressDialog dialog = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait");
            this.dialog.setIcon(R.drawable.logo);
            this.dialog.show();


        }

        @Override
        protected SoapObject doInBackground(String... strings) {

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
        final ListView lv1 = (ListView) getActivity().findViewById(R.id.listevent);
        lv1.setAdapter(new CustomeAdapter(listOfData, this.getContext()));
    }

    class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;

        AlphaAnimation inAnimation;
        AlphaAnimation outAnimation;

       /* FrameLayout progressBarHolder;

        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);*/

        @Override
        protected void onPreExecute() {
           /* inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            progressBarHolder.setAnimation(inAnimation);
            progressBarHolder.setVisibility(View.VISIBLE);*/
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
           /* outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);*/
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
    }



}

