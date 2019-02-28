package com.unnamed.studnetz.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class RequestQueueSingleton {

    // Code without ImageLoader from https://developer.android.com/training/volley/requestqueue.html
    // 26.02.2019

    private static volatile RequestQueueSingleton sSoleInstance;
    private RequestQueue mRequestQueue;
    private static Context sContext;

    private RequestQueueSingleton(Context context){
        sContext = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized RequestQueueSingleton getInstance(Context context){
        if(sSoleInstance == null){
            sSoleInstance = new RequestQueueSingleton(context);
        }

        return sSoleInstance;
    }

    public RequestQueue getRequestQueue(){
        if(mRequestQueue == null){
            mRequestQueue = Volley.newRequestQueue(sContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request req){
        getRequestQueue().add(req);
    }

}
