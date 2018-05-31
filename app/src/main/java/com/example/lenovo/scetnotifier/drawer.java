package com.example.lenovo.scetnotifier;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

public class drawer extends AppCompatActivity {

    private static final String SOAP_ACTION = "http://tempuri.org/showEvent";
    private static final String METHOD_NAME = "showEvent";
    private static final String NAMESPACE = "http://tempuri.org/";
    private static String urll = "";

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    Fragment fragment;
    FragmentManager fragmentManager;
    LinearLayout linearLayout;
    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        urll = getResources().getString(R.string.url);

        setContentView(R.layout.activity_drawer);

        SharedPreferences sharedPref = this.getSharedPreferences("Login", Context.MODE_PRIVATE);
        String lanSettings = sharedPref.getString("StudentID", null);


        try {
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            drawerLayout = (DrawerLayout) findViewById(R.id.DrawerLayout);
            linearLayout = (LinearLayout) findViewById(R.id.appbar);

            navigationView = (NavigationView) findViewById(R.id.Navigation);

            toggle = new ActionBarDrawerToggle(drawer.this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            };
            drawerLayout.setDrawerListener(toggle);
            toggle.syncState();
        } catch (Exception ex) {
            Toast.makeText(drawer.this, ex.toString(), Toast.LENGTH_LONG).show();
        }
//Navigation drawer menu method
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            public boolean onNavigationItemSelected(MenuItem item) {
                fragment = null;

                switch (item.getItemId()) {
                    case R.id.nav_Workshop:
                        fragment = new Workshop();
                        setTitle("Workshop");
                        break;
                    case R.id.nav_event:
                        fragment = new Event();
                        setTitle("Events");
                        break;
                    case R.id.nav_jobs:
                        fragment = new Placement();
                        setTitle("Placement");
                        break;
                    case R.id.nav_home:
                        fragment = new home();
                        setTitle("ScetNotifier");
                        break;
                    case R.id.nav_profile:
                        fragment = new MyAccount();
                        setTitle("MyAccount");
                        break;
                    case R.id.nav_feedback:
                        fragment = new Feedback();
                        setTitle("Feedback");
                        break;
                    case R.id.nav_chngpwd:
                        fragment = new ChangePassword();
                        setTitle("ChangePassword");
                        break;
                    case R.id.nav_fav:
                        fragment = new Favourite();
                        setTitle("Favourite");
                        break;
                    case R.id.nav_about_us:
                        fragment = new aboutus();
                        setTitle("AboutUS");
                        break;
                    case R.id.nav_logout:
                        SharedPreferences sharedPref = getSharedPreferences("Login", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.clear();
                        editor.commit();
                        finish();
                        Intent intent = new Intent(getApplicationContext(), login.class);
                        startActivity(intent);
                        break;


                    default:
                        SharedPreferences sharedPrefs = getSharedPreferences("Login", Context.MODE_PRIVATE);
                        SharedPreferences.Editor edi = sharedPrefs.edit();
                        edi.remove("ActivityList");
                        edi.apply();
                        finish();
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                setFragmentManager("0");

                return true;
            }

        });


        //bottom bar start
        bottomNavigation = (BottomNavigationView) findViewById(R.id.mainBottomNavigation);
        bottomNavigation.inflateMenu(R.menu.tabbar);

        HomeFragment();

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                fragment = null;
                switch (id) {
                    case R.id.action_favorites:
                        fragment = new Favourite();
                        setTitle("Favourite");
                        break;

                    case R.id.action_home:
                        fragment = new home();
                        setTitle("Home");
                        break;

                    case R.id.action_music:
                        fragment = new Notification();
                        setTitle("Notifications");
                        break;
                }
                setFragmentManager("0");
                return true;
            }
        });


    }

    public void HomeFragment() {

        Bundle extras = getIntent().getExtras();
        String ID = "";
        if(extras != null){

            String type = extras.getString("type");
            ID = extras.getString("ID");
            if(type.equals("event")){
                fragment = new DetailEvent();

            }

            else if(type.equals("job")){
                fragment = new DetailJob();

            }
            else if(type.equals("workshop")){
                fragment = new DetailWorkshop();

            }
            else{
                fragment = new home();
            }
        }
        else{
        fragment = new home();}
        setFragmentManager(ID);

    }

    public void setFragmentManager(String ID) {
        if (fragment != null) {
            fragmentManager = getSupportFragmentManager();
            Bundle b = new Bundle();
            b.putString("EventID",ID);
            fragment.setArguments(b);
            fragmentManager.beginTransaction().replace(R.id.grid, fragment).commit();
        }


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onBackPressed() {

        setAppDirection setDirection = new setAppDirection(this);
        String AppFragment = setDirection.getActivityDirection().toString();


        //String listActivity = sharedPrefs.getString("ActivityList", "test");


        fragment = null;

        switch (AppFragment) {
            case "event":
                fragment = new Event();
                setTitle("Event");
                break;
            case "workshop":
                fragment = new Workshop();
                break;
            case "placement":
                fragment = new Placement();
                break;
            default:
                SharedPreferences sharedPrefs = getSharedPreferences("Login", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.remove("ActivityList");
                editor.apply();

                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
                startActivity(intent);
                finish();
                System.exit(0);

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        setFragmentManager("0");
    }
}



       /* HomeFragment();

    }

    public void HomeFragment() {

        fragment = new home();
        setTitle("ScetNotifier");
        setFragmentManager();

    }

    public void setFragmentManager() {
        if (fragment != null) {
            fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.grid, fragment).commit();
        }

    }
*/

/*  @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        fragment = null;

        switch (item.getItemId()) {
            case R.id.nav_Workshop:
                fragment = new Workshop();
                setTitle("Workshop");
                break;
            case R.id.nav_event:
                fragment = new Event();
                setTitle("Events");
                break;
            case R.id.nav_jobs:
                fragment = new Placement();
                setTitle("Placement");
                break;
            case R.id.nav_home:
                fragment = new home();
                setTitle("ScetNotifier");
                break;
            case R.id.nav_profile:
                fragment = new MyAccount();
                setTitle("MyAccount");
                break;
            case R.id.nav_feedback:
                fragment = new Feedback();
                setTitle("Feedback");
                break;
            case R.id.nav_chngpwd:
                fragment = new ChangePassword();
                setTitle("ChangePassword");
                break;



            default:
                Toast.makeText(drawer.this, "No Data", Toast.LENGTH_LONG).show();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        setFragmentManager();

        return true;
    }*/



