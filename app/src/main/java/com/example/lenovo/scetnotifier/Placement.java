package com.example.lenovo.scetnotifier;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Placement extends Fragment {

    ListView listjob;
    private static CustomeAdapter adapter;
    Fragment fragment;
    FragmentManager fragmentManager;

    private static final String SOAP_ACTION = "http://tempuri.org/showJob";
    private static final String METHOD_NAME = "showJob";
    private static final String NAMESPACE = "http://tempuri.org/";
    private static String urll = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_placement, container, false);
        urll = v.getResources().getString(R.string.url);

        setAppDirection setDirection = new setAppDirection(getActivity());

        String lastDirection = setDirection.getLastDirection().toString();
        // && lastDirection.equals("-1") == false
        if (lastDirection.equals("placement") == false) {
            setDirection.setActivityDirection("placement");
        }

       /* listjob = (ListView) v.findViewById(R.id.listjob);
        adapter = new CustomeAdapter(getList(), v.getContext());
        listjob.setAdapter(adapter);*/
        new CallServices().execute("test");

        fragment = null;

        return v;


    /*protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job);*/
    }

    public void setFragmentManager(int jobID) {
        if (fragment != null) {
            fragmentManager = getActivity().getSupportFragmentManager();
            Bundle b = new Bundle();
            b.putString("jobID", String.valueOf(jobID));
            fragment.setArguments(b);

            fragmentManager.beginTransaction().replace(R.id.grid, fragment).commit();
        }

    }

    public ArrayList<DataModel> getList(SoapObject XmlResult) {


        ArrayList<DataModel> dmArray = new ArrayList<DataModel>();
        SoapObject table = null;


       /* DataModel dm = new DataModel();
        dm.setCompanyName("Name:Supplenta");
        dm.setComLocation("Location:Surat");
        dm.setComSkills("Skills:ASP.NET");
        dmArray.add(dm);

        dm = new DataModel();
        dm.setCompanyName("Name:Finlogic");
        dm.setComLocation("Location:mumbai");
        dm.setComSkills("Skills:PHP");
        dmArray.add(dm);

        dm = new DataModel();
        dm.setCompanyName("Name:Google");
        dm.setComLocation("Location:Banglore");
        dm.setComSkills("Skills:Android");
        dmArray.add(dm);

        dm = new DataModel();
        dm.setCompanyName("Name:Microsoft");
        dm.setComLocation("Location:mumbai");
        dm.setComSkills("Skills:PHP");
        dmArray.add(dm);

        dm = new DataModel();
        dm.setCompanyName("Name:Essar steel");
        dm.setComLocation("Location:Pune");
        dm.setComSkills("Skills:ASP.Net");
        dmArray.add(dm);

        dm = new DataModel();
        dm.setCompanyName("Name:Thomsan");
        dm.setComLocation("Location:Vadodara");
        dm.setComSkills("Skills:MVC");
        dmArray.add(dm);


*/

        if (XmlResult.getPropertyCount() > 0) {
            for (int i = 0; i < XmlResult.getPropertyCount(); i++) {
                table = (SoapObject) XmlResult.getProperty(i);
                DataModel dm = new DataModel();
                dm.setCompanyName(table.getProperty("CompanyName").toString());
                dm.setComSkills(table.getProperty("Skills").toString());
                dm.setjobID(Integer.parseInt(table.getProperty("jobID").toString()));
                dm.setcomimg(table.getProperty("CompanyImage").toString());

                dmArray.add(dm);

            }

        }
        return dmArray;
    }

//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//        String item = parent.getItemAtPosition(position).toString();
//        Toast.makeText(getActivity(), "CLICK: " + item, Toast.LENGTH_SHORT).show();
//    }


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
                        inflate(R.layout.joblist, parent, false);

                holder = new Holder();
                holder.txtComName = (TextView) convertView.findViewById(R.id.txtViewCompanyName);
                //   holder.txtComLoc = (TextView) convertView.findViewById(R.id.txtViewLocation);
                holder.txtComSkills = (TextView) convertView.findViewById(R.id.txtViewSkills);
                holder.btnviewjob = (Button) convertView.findViewById(R.id.btnviewjob);
                holder.jobID = (TextView) convertView.findViewById(R.id.txtjobID);
                holder.comimg = (ImageView) convertView.findViewById(R.id.Company_image);

                convertView.setTag(holder);

            } else {
                holder = (Holder) convertView.getTag();
            }
            d = dm.get(position);
            String str1 = d.getCompanyName();
            holder.txtComName.setText(d.getCompanyName());
            holder.jobID.setText(String.valueOf(d.getjobID()));
            // holder.txtComLoc.setText(d.getComLocation());
            holder.txtComSkills.setText(d.getComSkills());

            if (holder.comimg != null) {

                byte[] decodedstring= Base64.decode(d.getcomimg(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedstring,0,decodedstring.length);
                //final Bitmap circularBitmap = imageconverter.getRoundedCornerBitmap(decodedByte,65);
                holder.comimg.setImageBitmap(decodedByte);
            }

            /*new Thread(new Runnable() {
                @Override
                public void run() {
                    String imgURL = dm.get(position).getcomimg();

                    final Bitmap bitImage = getBitmapFromURL(imgURL);

                    holder.comimg.post(new Runnable() {
                        @Override
                        public void run() {
                            holder.comimg.setImageBitmap(bitImage);
                        }
                    });
                }
            }).start();

*/
            holder.btnviewjob.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragment = new DetailJob();
                    setFragmentManager(Integer.parseInt(holder.jobID.getText().toString()));
                }
            });
            return convertView;
        }

        public class Holder {
            TextView txtComName;
            //  TextView txtComLoc;
            TextView txtComSkills;
            TextView jobID;
            Button btnviewjob;
            ImageView comimg;
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

        protected void onPostExecute(SoapObject result) {
            afterCallReturn(result);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

        }


    }

    private void afterCallReturn(SoapObject result) {
        ArrayList<DataModel> listOfData = getList(result);
        final ListView lv1 = (ListView) getActivity().findViewById(R.id.listjob);
        lv1.setAdapter(new CustomeAdapter(listOfData, this.getContext()));
    }


}

