package com.example.lenovo.scetnotifier;


import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by LENOVO on 19-12-2017.
 */
public class setAppDirection {

    private final Context context;

    //back activity works on activity to activity
    //this is code for back from fragment to fragment
    //calling from which activity? here drawer
    public setAppDirection(Context context) {
        this.context = context;
    }

    public void setActivityDirection(String Val) {

        /*SharedPreferences sharedPref = this.context.getSharedPreferences("Login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("ActivityList", Val);
        editor.commit();*/
        SharedPreferences sharedPrefs = this.context.getSharedPreferences("Login", Context.MODE_PRIVATE);

        //??
        String oldValue = sharedPrefs.getString("ActivityList", null);

        String appendedValue = "";
        if (oldValue != null) {
            if (oldValue.equals("")) {
                appendedValue = Val;
            } else {
                appendedValue = oldValue + ";" + Val;
            }
        } else {
            appendedValue = Val;
        }
       /* if (oldValue != null && oldValue.equals("") == false) {

        } else {

        }*/

        setSharedPrefrences(appendedValue);
    }

    public String getActivityDirection() {
        SharedPreferences sharedPrefs = context.getSharedPreferences("Login", Context.MODE_PRIVATE);
        String listActivity = sharedPrefs.getString("ActivityList", null);
        String newValue = "";
        if (listActivity != null) {
            if (listActivity.equals("") || listActivity.equals("home")) {
                return "-1";
            } else {
                String arr[] = listActivity.split(";");
                if (arr.length == 0) {
                    return "-1";
                } else if (arr.length == 1) {
                    setSharedPrefrences("-1");
                    return "-1";
                } else {
                    for (int i = 0; i < arr.length; i++) {
                        if (i != (arr.length - 1)) {
                            if (newValue.equals("")) {
                                newValue = arr[i].trim();
                            } else {
                                newValue = newValue + ";" + arr[i].trim();
                            }
                        }
                    }
                    setSharedPrefrences(newValue);
                    //return previous frag ex..if we have 4 frag it will return 1st 3 frag
                    return arr[arr.length - 2].toString();
                }
            }
        } else {
            return "-1";
        }
    }

    private void setSharedPrefrences(String valPref) {
        SharedPreferences sharedPrefs = context.getSharedPreferences("Login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString("ActivityList", valPref);
        editor.commit();
    }


    //for getting last direction activitylist na record ne - thi split karavana(agar lenth> 0 hoy to)
    //last je hoy te return karavanu
    public String getLastDirection() {
        SharedPreferences sharedPrefs = context.getSharedPreferences("Login", Context.MODE_PRIVATE);
        String listActivity = sharedPrefs.getString("ActivityList", null);
        if (listActivity != null) {
            if (listActivity.equals("")) {
                return "-1";
            } else {
                String arr[] = listActivity.split(";");
                if (arr.length > 0) {
                    return arr[arr.length - 1].toString();
                } else {
                    return "-1";
                }
            }
        } else {
            return "-1";
        }
    }


}
