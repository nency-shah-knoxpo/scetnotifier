package com.example.lenovo.scetnotifier;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class aboutus extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_aboutus, container, false);

        TextView abus;
        abus = (TextView)v.findViewById(R.id.txteditabout);
        String formattedText = "Vision\n" +
                "To evolve into a shrine of excellence in technical education & research and an aspiring place for engineering education seekers and educators." +
                " We commit to produce knowledge workers, stimulated by discipline, " +
                "professional ethics, continuous learning, leadership skills and elevated morals, striving towards progressive civilization.<br />" ;
        abus.setText(Html.fromHtml(formattedText));

        return  v;


    }
}
