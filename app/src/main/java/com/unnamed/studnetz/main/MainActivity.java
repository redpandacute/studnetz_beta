package com.unnamed.studnetz.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.unnamed.studnetz.LoginRegister.LoginRegisterActivity;
import com.unnamed.studnetz.R;
import com.unnamed.studnetz.main.fragments.GroupsFragment;
import com.unnamed.studnetz.main.fragments.HomeFragment;
import com.unnamed.studnetz.main.fragments.NotificationsFragment;
import com.unnamed.studnetz.main.fragments.ProfileFragment;
import com.unnamed.studnetz.main.fragments.SearchFilterFragment;
import com.unnamed.studnetz.network.ManagerSingleton;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, HomeFragment.OnFragmentInteractionListener {

    BottomNavigationView bottomNavigation;
    ImageButton SearchButton;

    Button mLogOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigation = findViewById(R.id.navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(this);
        mLogOutButton = findViewById(R.id.temp_logout);
        mLogOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ManagerSingleton.getInstance(getApplicationContext()).logOut();
                Intent loginIntent = new Intent(MainActivity.this, LoginRegisterActivity.class);
                startActivity(loginIntent);
                finish();
            }
        });

        replaceFragment(new HomeFragment(),false);

    }

    private boolean replaceFragment(Fragment fragment, boolean addToBackStack){
        if(fragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.main_fragment_container, fragment);

            if(addToBackStack){
                fragmentTransaction.addToBackStack(null);
            }

            fragmentTransaction.commit();
            return true;
        }
        return false;
    }

    // Sets active Fragment if the selected menuItem of the BottomNavigationBar is changed
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;

        switch (menuItem.getItemId()){

            case R.id.navigation_home:
                fragment = new HomeFragment();
                break;


            case R.id.navigation_groups:
                fragment = new GroupsFragment();
                break;

            case R.id.navigation_notifications:
                fragment = new NotificationsFragment();
                break;

            case R.id.navigation_profile:
                fragment = new ProfileFragment();
                break;
        }

        return replaceFragment(fragment, false);
    }

    @Override
    public void onHomeFragmentInteraction() {
        replaceFragment(new SearchFilterFragment(), true);
    }
}
