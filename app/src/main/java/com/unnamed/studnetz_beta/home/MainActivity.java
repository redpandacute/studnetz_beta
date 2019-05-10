package com.unnamed.studnetz_beta.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.unnamed.studnetz_beta.R;

public class MainActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener, GroupsFragment.OnFragmentInteractionListener, MessengerFragment.OnFragmentInteractionListener, ProfileFragment.OnFragmentInteractionListener {

    BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null){
            HomeFragment homeFragment = new HomeFragment().newInstance();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.fl_container_main, homeFragment);
            fragmentTransaction.commit();
        }else{

        }

        mBottomNavigationView = findViewById(R.id.bottom_navigation_main);
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_groups:
                        replaceFragment(new GroupsFragment().newInstance());
                        return true;
                    case R.id.action_notifications:
                        replaceFragment(new MessengerFragment().newInstance());
                        return true;

                    default:
                        replaceFragment(new HomeFragment().newInstance());
                        return true;
                }
            }
        });

    }

    private void replaceFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fl_container_main,fragment);
        fragmentTransaction.commit();
    }
}
