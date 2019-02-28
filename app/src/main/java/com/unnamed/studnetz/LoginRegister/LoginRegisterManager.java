package com.unnamed.studnetz.LoginRegister;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.unnamed.studnetz.network.RequestQueueSingleton;

import java.io.Serializable;

public class LoginRegisterManager implements Serializable{

    public static final String REQUEST_TAG = "LOGIN_REGISTER_TAG";
    private RequestQueue mRequestQueue;

    private Context mContext;

    public LoginRegisterManager(Context ctx){
        this.mContext = ctx;
        mRequestQueue = RequestQueueSingleton.getInstance(mContext).getRequestQueue();
    }
    
    public void onStop(){
        if(mRequestQueue != null){
            mRequestQueue.cancelAll(REQUEST_TAG);
        }
    }

}
