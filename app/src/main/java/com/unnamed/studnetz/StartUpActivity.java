package com.unnamed.studnetz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.unnamed.studnetz.LoginRegister.LoginRegisterActivity;
import com.unnamed.studnetz.main.MainActivity;
import com.unnamed.studnetz.network.ManagerSingleton;

public class StartUpActivity extends AppCompatActivity {

    private final static String LOG_TAG = "StartUpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);


    }

    @Override
    protected void onStart() {
        super.onStart();

        ManagerSingleton.getInstance(getApplicationContext()).autoLogin(new ManagerSingleton.autoLoginCallback() {
            @Override
            public void onSuccess() {

                Log.d(LOG_TAG, "AutoLogin successful");
                sendToMainActivity();
            }

            @Override
            public void onError(String error) {
                sendToLoginActivity();
                Log.d(LOG_TAG, "AutoLogin unsuccessful, Error: " + error);
            }
        });
    }

    private void sendToMainActivity(){
        Intent mainIntent = new Intent(StartUpActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }

    private void sendToLoginActivity(){
        Intent mainIntent = new Intent(StartUpActivity.this, LoginRegisterActivity.class);
        startActivity(mainIntent);
        finish();
    }

}
