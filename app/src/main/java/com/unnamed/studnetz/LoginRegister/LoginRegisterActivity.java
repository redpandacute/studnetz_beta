package com.unnamed.studnetz.LoginRegister;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.unnamed.studnetz.LoginRegister.login.LoginFragment;
import com.unnamed.studnetz.LoginRegister.register.RegisterFragment;
import com.unnamed.studnetz.LoginRegister.register.RegisterFragmentChildInteractionListener;
import com.unnamed.studnetz.R;
import com.unnamed.studnetz.network.SingletonRequestQueue;

public class LoginRegisterActivity extends AppCompatActivity implements LoginFragment.onLoginFragmentInteractionListener, RegisterFragment.onRegisterFragmentInteractionListener, RegisterFragmentChildInteractionListener {

    private LoginFragment mLoginFragment;
    private RegisterFragment mRegisterFragment;

    private final static String LOGIN_FRAGMENT_TAG = "LOGIN_FRAGMENT_TAG";
    private final static String REGISTER_FRAGMENT_TAG = "REGISTER_FRAGMENT_TAG";

    FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

        mLoginFragment = new LoginFragment();
        mRegisterFragment = new RegisterFragment();

        fm = getSupportFragmentManager();

        if(savedInstanceState == null) {

            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.add(R.id.flcontainer_login_register_fragment, mLoginFragment);
            fragmentTransaction.commitNow();

        }else{

            mLoginFragment = (LoginFragment) fm.findFragmentByTag(LOGIN_FRAGMENT_TAG);
            mRegisterFragment = (RegisterFragment) fm.findFragmentByTag(REGISTER_FRAGMENT_TAG);

        }
    }


    @Override
    public void onLoginButtonPressed(View v) {
        if(v == null){
            return;
        }

        switch (v.getId()){
            case R.id.text_login_forgot:
                Toast.makeText(getApplicationContext(),"Open Forgot Password", Toast.LENGTH_LONG).show();
                break;
            case R.id.text_login_register:
                replaceFragment(mRegisterFragment, true);
                break;
        }

    }

    @Override
    public void onRegisterButtonPressed(View v) {
        switch (v.getId()){
            case R.id.register_login:
                replaceFragment(mLoginFragment, false, null);
                break;

        }
    }


    private void replaceFragment(Fragment fragment, boolean addToBackStack){
        if(fragment == null){
            return;
        }
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.flcontainer_login_register_fragment, fragment);

        if(addToBackStack)
            fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();
    }

    private void replaceFragment(Fragment fragment, boolean addToBackStack, String clearBackStackUpTo){
        fm.popBackStack(clearBackStackUpTo ,FragmentManager.POP_BACK_STACK_INCLUSIVE);

        replaceFragment(fragment,addToBackStack);
    }

    // Methods for communication between RegisterFragment and child fragments
    @Override
    public void nextRegisterChildFragment(String[] data) {
        mRegisterFragment.nextFragment(data);
    }

    @Override
    public void backRegisterChildFragment() {
        mRegisterFragment.backFragment();
    }
}
