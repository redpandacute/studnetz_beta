package com.unnamed.studnetz.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.unnamed.studnetz.models.ClientProfileModel.ClientProfileModel;
import com.unnamed.studnetz.network.requests.AutoAuthenticationRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class ManagerSingleton {

    // Code without ImageLoader from https://developer.android.com/training/volley/requestqueue.html
    // 26.02.2019


    private final static String LOG_TAG = "ManagerSingleton";
    private static final String SHARED_PREFERENCE_FILE = "com.unnamed.studnetz.LOGIN_TOKEN";

    private static volatile ManagerSingleton sSoleInstance;
    private RequestQueue mRequestQueue;
    private static Context sContext;

    private boolean mLogedIn = false;

    private ManagerSingleton(Context context){
        sContext = context;
        mRequestQueue = getRequestQueue();
    }

    private static SharedPreferences getPreferences(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static synchronized ManagerSingleton getInstance(Context context){
        if(sSoleInstance == null){
            sSoleInstance = new ManagerSingleton(context);
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

    public void setActiveUser(String token){
        mLogedIn = !TextUtils.isEmpty(token);

        SharedPreferences.Editor editor = getPreferences(sContext).edit();
        editor.putString(SHARED_PREFERENCE_FILE, token);
        editor.apply();
    }

    public void logOut(){
        mLogedIn = false;

        SharedPreferences.Editor editor = getPreferences(sContext).edit();
        editor.putString(SHARED_PREFERENCE_FILE, "");
        editor.apply();
    }

    public String getActiveUser(){
        return getPreferences(sContext).getString(SHARED_PREFERENCE_FILE, "");
    }

    public void autoLogin(final autoLoginCallback callback){
        String token = getActiveUser();
        if(TextUtils.isEmpty(token)){
            callback.onError("Token Empty");
            return;
        }
        sendAutoLoginRequest(token,callback);
    }

    private void sendAutoLoginRequest(String token, final autoLoginCallback callback){

        AutoAuthenticationRequest autoAuthenticationRequest = new AutoAuthenticationRequest(token, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject answer = new JSONObject(response);
                    if(answer.getBoolean("success")){
                        callback.onSuccess();
                    }else{
                        callback.onError(answer.getString("error"));
                    }
                }catch (JSONException e){
                    Log.e(LOG_TAG, "JSON Exception", e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(LOG_TAG,  "AutoLoginRequest Volley Error", error);
            }
        });

        addToRequestQueue(autoAuthenticationRequest);
        Log.d(LOG_TAG,"Start AutoLoginRequest");

    }

    public interface autoLoginCallback{
        void onSuccess();
        void onError(String error);
    }

}
