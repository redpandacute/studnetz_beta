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
import com.unnamed.studnetz.R;

public class LoginRegisterActivity extends AppCompatActivity implements RegisterFragment.RegisterFragmentInterface {

    LoginFragment mLoginFragment;
    final static String LOGIN_FRAGMENT_TAG = "login_fragment";

    FragmentManager fm;

    private RegisterFragment mRegisterFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

         fm = getSupportFragmentManager();

        if(savedInstanceState == null) {
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.add(R.id.login_register_fragment_container, new LoginFragment(),LOGIN_FRAGMENT_TAG);
            fragmentTransaction.commitNow();
        }else{
            mLoginFragment = (LoginFragment) fm.findFragmentByTag(LOGIN_FRAGMENT_TAG);
        }
    }

    private void replaceFragment(Fragment fragment, boolean addToBackStack){
        if(fragment == null){
            return;
        }
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.login_register_fragment_container, fragment);
        if(addToBackStack)
            fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

  /*  public void ChangeFragment(Fragment fragment,boolean addToBackStack) {
        replaceFragment(fragment, addToBackStack);
    }
*/ //TODO: chammer glaubs useneh und andersch löse via interface


    @Override
    public void onRegistration() {
        //nur es biispiel zum teste
    }

    @Override
    public void onLoginSelected() {
        //TODO: CHANGE TO LOGIN FRAGMENT
        //(lösig via interface xD), mer chönti aber au e allgemeini Change Fragment methode mache, ich glaubs aber so wäris schöner.
    }
}
