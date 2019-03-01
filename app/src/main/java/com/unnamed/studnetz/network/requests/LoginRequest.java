package com.unnamed.studnetz.network.requests;

import android.support.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest {

    private static final String URL = "https://deep-contact-232012.appspot.com/login.php";
    private Map<String, String> mParams;

    public LoginRequest(Map<String, String> input, Response.Listener<String> listener, @Nullable Response.ErrorListener errorListener) {
        super(Method.POST, URL, listener, errorListener);
        mParams = input;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mParams;
    }
}
