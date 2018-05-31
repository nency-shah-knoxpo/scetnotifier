package com.example.lenovo.scetnotifier;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

public class mybottombar extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;
    Fragment fragment;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mybottombar);
        bottomNavigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigation.inflateMenu(R.menu.tabbar);

        HomeFragment();

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                fragment = null;
                switch (id) {
                    case R.id.action_favorites:
                        fragment = new Event();
                        break;
                    case R.id.action_home:
                        fragment = new home();
                        break;
                    case R.id.action_music:
                        fragment = new Workshop();
                        break;
                }
                setFragmentManager();
                return true;
            }
        });

    }

    public void HomeFragment() {

        fragment = new Event();
        setFragmentManager();

    }

    public void setFragmentManager() {
        if (fragment != null) {
            fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.main_container, fragment).commit();
        }



    }
}
