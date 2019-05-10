package com.unnamed.studnetz_beta.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.unnamed.studnetz_beta.R;


public class MainActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener, GroupsFragment.OnFragmentInteractionListener, MessengerFragment.OnFragmentInteractionListener, ProfileFragment.OnFragmentInteractionListener {

    BottomNavigationView mBottomNavigationView;

    FragmentManager fm;

    GroupsFragment mGroupFragment;
    HomeFragment mHomeFragment;
    MessengerFragment mMessengerFragment;
    ProfileFragment mProfileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fm = getSupportFragmentManager();

        MaterialToolbar toolbar = findViewById(R.id.material_toolbar_main);
        setSupportActionBar(toolbar);

        mGroupFragment = GroupsFragment.newInstance();
        mMessengerFragment = MessengerFragment.newInstance();
        mProfileFragment = ProfileFragment.newInstance();
        mHomeFragment = HomeFragment.newInstance();

        assert getSupportActionBar() != null;

        if(savedInstanceState == null){
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.add(R.id.fl_container_main, mHomeFragment);
            fragmentTransaction.commit();
            getSupportActionBar().setTitle(getResources().getString(R.string.activity_main_menu_home));
        }

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_profile:
                        replaceFragment(mProfileFragment,true);
                        break;

                    case R.id.action_settings:
                        // TODO: Create Settings Fragment
                        break;
                }
                return false;
            }
        });


        getSupportActionBar().setTitle(getResources().getString(R.string.activity_main_menu_home));

        mBottomNavigationView = findViewById(R.id.bottom_navigation_main);
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                assert getSupportActionBar() != null;
                switch (item.getItemId()){
                    case R.id.action_groups:
                        replaceFragment(mGroupFragment);
                        getSupportActionBar().setTitle(R.string.activity_main_menu_groups);
                        return true;
                    case R.id.action_notifications:
                        replaceFragment(mMessengerFragment);
                        getSupportActionBar().setTitle(R.string.activity_main_menu_notifications);
                        return true;

                    default:
                        replaceFragment(mHomeFragment);
                        getSupportActionBar().setTitle(R.string.activity_main_menu_home);
                        return true;
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_toolbar_main, menu);

        return true;
    }

    private void replaceFragment(Fragment fragment){
        replaceFragment(fragment, false);
    }

    private void replaceFragment(Fragment fragment, boolean addToBackstack){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fl_container_main,fragment);
        if(addToBackstack){
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }

}
