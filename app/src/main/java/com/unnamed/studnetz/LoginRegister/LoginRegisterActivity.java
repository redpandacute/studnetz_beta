package com.unnamed.studnetz.LoginRegister;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.unnamed.studnetz.LoginRegister.login.LoginFragment;
import com.unnamed.studnetz.LoginRegister.register.RegisterFragment;
import com.unnamed.studnetz.R;
import com.unnamed.studnetz.main.MainActivity;
import com.unnamed.studnetz.network.ManagerSingleton;

public class LoginRegisterActivity extends AppCompatActivity implements LoginFragment.onLoginFragmentInteractionListener, RegisterFragment.onRegisterFragmentInteractionListener{

    private final static String LOG_TAG = "LoginRegisterActivity";

    private final static String LOGIN_FRAGMENT_TAG = "LOGIN_FRAGMENT_TAG";
    private final static String REGISTER_FRAGMENT_TAG = "REGISTER_FRAGMENT_TAG";

    private LoginFragment mLoginFragment;
    private RegisterFragment mRegisterFragment;

    private FragmentManager fm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

        mLoginFragment = new LoginFragment();
        mRegisterFragment = new RegisterFragment();

        fm = getSupportFragmentManager();

        if(savedInstanceState == null) {
            Log.d(LOG_TAG,"Starting Login Fragment");
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

        switch (v.getId()){
            case R.id.text_login_forgot:
                //Todo: Forgot Password
                Toast.makeText(getApplicationContext(),"Open Forgot Password", Toast.LENGTH_LONG).show();
                break;
            case R.id.text_login_register:
                replaceFragment(mRegisterFragment, true);
                break;
        }

    }

    @Override
    public void onLoginProcessComplete() {
        sendToMainActivity();
    }

    @Override
    public void onRegisterButtonPressed(View v) {
        switch (v.getId()){
            case R.id.register_login:
                replaceFragment(mLoginFragment, false, null);
                break;

        }
    }

    @Override
    public void onRegisterProcessComplete() {
        sendToMainActivity();
    }

    private void sendToMainActivity(){
        Intent mainIntent = new Intent(LoginRegisterActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }


    private void replaceFragment(Fragment fragment, boolean addToBackStack){
        Log.d(LOG_TAG,"Replacing LoginRegisterFragment with " + fragment.toString());

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

    @Override
    protected void onStop() {
        super.onStop();
    }

}
