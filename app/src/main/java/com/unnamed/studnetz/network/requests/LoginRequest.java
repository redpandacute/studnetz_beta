package com.unnamed.studnetz.network.requests;

import android.support.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest {

    private static final String URL = "https://deep-contact-232012.appspot.com/login_jwt.php";
    private Map<String, String> mParams;

    public LoginRequest(String email, String password, Response.Listener<String> listener, @Nullable Response.ErrorListener errorListener) {
        super(Method.POST, URL, listener, errorListener);
        mParams = new HashMap<String, String>();
        mParams.put("email", email);
        mParams.put("password", password);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mParams;
    }
}
