package com.example.lenovo.scetnotifier;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by LENOVO on 27-02-2018.
 */
public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    TelephonyManager telephonyManager;
    String IMEINumber;

    //this method will be called
    //when the token is generated
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();


        //now we will have the token
        String token = FirebaseInstanceId.getInstance().getToken();

        //for now we are displaying the token in the log
        //copy it as this method is called only when the new token is generated
        //and usually new token is only generated when the app is reinstalled or the data is cleared
        Log.d("MyRefreshedToken", token);

        telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        IMEINumber = telephonyManager.getDeviceId();

        Log.d("MyIMEINo", IMEINumber);

        sendToken(token, IMEINumber);
    }

    public void sendToken(String _Token, String _IMEINum) {
        new AsyncStoreIMEIReferenceToken().execute(_Token, _IMEINum);
    }



}
