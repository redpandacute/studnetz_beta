package com.unnamed.studnetz.LoginRegister.login;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.unnamed.studnetz.LoginRegister.register.RegisterFragment;
import com.unnamed.studnetz.R;
import com.unnamed.studnetz.main.fragments.HomeFragment;
import com.unnamed.studnetz.network.SingletonRequestQueue;
import com.unnamed.studnetz.network.requests.LoginRequest;

public class LoginFragment extends Fragment implements View.OnClickListener {

    public static final String REQUEST_TAG = "LoginTag";

    public interface onLoginFragmentInteractionListener {
        void onLoginButtonPressed(View v);
    }

    private onLoginFragmentInteractionListener mListener;

    ProgressBar mLoginProgressBar;

    private TextView mLoginErrorText;
    private EditText mPasswordField;
    private EditText mEmailField;

    private RequestQueue mRequestQueue;

    private boolean loginIn = false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, null);

        TextView mButtonLoginSignUp = view.findViewById(R.id.text_login_register);
        mButtonLoginSignUp.setOnClickListener(this);
        TextView buttonForgotPassword = view.findViewById(R.id.text_login_forgot);
        buttonForgotPassword.setOnClickListener(this);

        Button mLoginButton = view.findViewById(R.id.button_login_signin);
        mLoginButton.setOnClickListener(this);

        mLoginProgressBar = view.findViewById(R.id.login_progressBar);

        mPasswordField = view.findViewById(R.id.edittext_login_password);
        mEmailField = view.findViewById(R.id.edittext_login_email);
        mLoginErrorText = view.findViewById(R.id.text_error_login);

        mRequestQueue = SingletonRequestQueue.getInstance(view.getContext()).getRequestQueue();

        return view;

    }

    @Override
    public void onClick(View v) {
        if(!loginIn) {
            if (v.getId() == R.id.button_login_signin) {
                login();
            } else {
                mListener.onLoginButtonPressed(v);
            }
        }
    }

    private void login()  {

        String mLoginEmail = mEmailField.getText().toString();
        String mLoginPassword = mPasswordField.getText().toString();

        if(!TextUtils.isEmpty(mLoginEmail) && !TextUtils.isEmpty(mLoginPassword)){

            mLoginProgressBar.setVisibility(View.VISIBLE);

            loginIn = true;
            LoginRequest loginRequest = new LoginRequest(mLoginEmail, mLoginPassword, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    mLoginErrorText.setText(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    //TODO: Error handling
                    mLoginErrorText.setText(error.toString());
                }
            });
            loginRequest.setTag(REQUEST_TAG);

            SingletonRequestQueue.getInstance(this.getContext()).addToRequestQueue(loginRequest);

            mLoginProgressBar.setVisibility(View.INVISIBLE);
            loginIn = false;

        }else{
            mLoginErrorText.setText(R.string.input_field_empty_error);
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if( context instanceof onLoginFragmentInteractionListener){
            mListener = (onLoginFragmentInteractionListener) context;
        }else{
            throw new RuntimeException(context.toString()
                    + "must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mRequestQueue != null){
            mRequestQueue.cancelAll(REQUEST_TAG);
        }
    }
}
