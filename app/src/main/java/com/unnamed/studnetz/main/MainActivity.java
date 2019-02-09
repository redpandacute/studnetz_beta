package com.unnamed.studnetz.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.unnamed.studnetz.R;
import com.unnamed.studnetz.main.fragments.GroupsFragment;
import com.unnamed.studnetz.main.fragments.HomeFragment;
import com.unnamed.studnetz.main.fragments.NotificationsFragment;
import com.unnamed.studnetz.main.fragments.ProfileFragment;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, FragmentManager.OnBackStackChangedListener {

    BottomNavigationView bottomNavigation;

    final static String MY_FRAGMENT_TAG = "active_fragment";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigation = findViewById(R.id.navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(this);

        replaceFragment(new HomeFragment());

        getSupportFragmentManager().addOnBackStackChangedListener(this);

    }

    private boolean replaceFragment(Fragment fragment){
        if(fragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.main_fragment_container, fragment,MY_FRAGMENT_TAG);

            if(getSupportFragmentManager().findFragmentByTag(MY_FRAGMENT_TAG) != null) {
                fragmentTransaction.addToBackStack(null);
            }

            fragmentTransaction.commit();
            return true;
        }
        return false;
    }

    // Sets active Fragment if the selected menuItem of the BottomNavigationBar is changed
    // Also makes sure that the active fragment is not reset if it is equal to the menuItem
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;
        Fragment activeFragment = getSupportFragmentManager().findFragmentByTag(MY_FRAGMENT_TAG);

        switch (menuItem.getItemId()){

            case R.id.navigation_home:
                if(activeFragment instanceof HomeFragment)
                    return true;
                fragment = new HomeFragment();
                break;


            case R.id.navigation_groups:
                if(activeFragment instanceof GroupsFragment)
                    return true;
                fragment = new GroupsFragment();
                break;

            case R.id.navigation_notifications:
                if(activeFragment instanceof NotificationsFragment)
                    return true;
                fragment = new NotificationsFragment();
                break;

            case R.id.navigation_profile:
                if(activeFragment instanceof ProfileFragment)
                    return true;
                fragment = new ProfileFragment();
                break;
        }

        return replaceFragment(fragment);
    }


    // Sets selected Item of bottomNavigationBar if back button is pressed
    // that it matches the active fragment
    @Override
    public void onBackStackChanged() {
        Fragment activeFragment = getSupportFragmentManager().findFragmentByTag(MY_FRAGMENT_TAG);

        if (activeFragment instanceof GroupsFragment) {
            if(bottomNavigation.getSelectedItemId() != R.id.navigation_groups)
            bottomNavigation.setSelectedItemId(R.id.navigation_groups);
        } else if (activeFragment instanceof NotificationsFragment) {
            if(bottomNavigation.getSelectedItemId() != R.id.navigation_notifications)
            bottomNavigation.setSelectedItemId(R.id.navigation_notifications);
        } else if (activeFragment instanceof ProfileFragment) {
            if(bottomNavigation.getSelectedItemId() != R.id.navigation_profile)
            bottomNavigation.setSelectedItemId(R.id.navigation_profile);
        } else {
            if(bottomNavigation.getSelectedItemId() != R.id.navigation_home)
            bottomNavigation.setSelectedItemId(R.id.navigation_home);
        }
    }
}
