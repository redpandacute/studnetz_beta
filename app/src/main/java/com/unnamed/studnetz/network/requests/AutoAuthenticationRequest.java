package com.unnamed.studnetz.network.requests;

import android.support.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class AutoAuthenticationRequest extends StringRequest {

    private static final String URL = "https://deep-contact-232012.appspot.com/auto-authentication.php";
    private Map<String, String> mParams;

    public AutoAuthenticationRequest(String token, Response.Listener<String> listener, @Nullable Response.ErrorListener errorListener) {
        super(Request.Method.POST, URL, listener, errorListener);
        mParams = new HashMap<String, String>();
        mParams.put("token", token);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mParams;
    }
}