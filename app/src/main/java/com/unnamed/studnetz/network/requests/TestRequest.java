package com.unnamed.studnetz.network.requests;

import android.support.annotation.Nullable;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class TestRequest extends StringRequest {

    private static final String URL = "https://deep-contact-232012.appspot.com/test.php";

    public TestRequest(Response.Listener<String> listener, @Nullable Response.ErrorListener errorListener) {
        super(Method.POST, URL, listener, errorListener);
    }

}
