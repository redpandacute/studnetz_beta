package com.unnamed.studnetz.LoginRegister.login;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.unnamed.studnetz.R;
import com.unnamed.studnetz.network.RequestQueueSingleton;
import com.unnamed.studnetz.network.requests.LoginRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginFragment extends Fragment implements View.OnClickListener {

    private onLoginFragmentInteractionListener mListener;

    private final static String LOG_TAG = "LoginFragment";
    public static final String REQUEST_TAG = "LOGIN_REQUEST_TAG";

    private ProgressBar mLoginProgressBar;

    private TextView mLoginErrorText;
    private EditText mPasswordField;
    private EditText mEmailField;

    private TextView mButtonLoginSignUp;
    private TextView mButtonForgotPassword;
    private Button mLoginButton;

    private RequestQueue mRequestQueue;

    private InputMethodManager mInputMethodManager;

    private boolean mLoginIn = false;
    private String mLoginError = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        mInputMethodManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);

        mRequestQueue = RequestQueueSingleton.getInstance(getContext()).getRequestQueue();

        mLoginProgressBar = view.findViewById(R.id.login_progressBar);
        mPasswordField = view.findViewById(R.id.edittext_login_password);
        mEmailField = view.findViewById(R.id.edittext_login_email);
        mLoginErrorText = view.findViewById(R.id.text_error_login);
        mLoginButton = view.findViewById(R.id.button_login_signin);

        mButtonForgotPassword = view.findViewById(R.id.text_login_forgot);
        mButtonLoginSignUp = view.findViewById(R.id.text_login_register);

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mButtonLoginSignUp.setOnClickListener(this);
        mButtonForgotPassword.setOnClickListener(this);
        mLoginButton.setOnClickListener(this);
        // If enter ist pressed on last input field the same happens as if the user pressed the next button
        mPasswordField.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN){
                    switch (keyCode){
                        case KeyEvent.KEYCODE_DPAD_DOWN:
                        case KeyEvent.KEYCODE_ENTER:
                            login();
                            break;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
            if(!mLoginIn) {
                if (v.getId() == R.id.button_login_signin) {
                    // Hide soft keyboard
                    if(mInputMethodManager != null)
                        mInputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    login();
                } else {
                    mListener.onLoginButtonPressed(v);
                }
            }

    }

    private void login()  {
        Log.d(LOG_TAG,"Start login process");

        String loginEmail = mEmailField.getText().toString();
        String loginPassword = mPasswordField.getText().toString();

        if(!TextUtils.isEmpty(loginEmail) && !TextUtils.isEmpty(loginPassword)){

            mLoginIn = true;
            mLoginProgressBar.setVisibility(View.VISIBLE);

            sendLoginRequest(loginEmail, loginPassword, new LoginRequestCallback() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    // Todo: Set active User
                    mLoginError = "";
                    Log.d(LOG_TAG, "Login successful, User:" + jsonObject.toString());

                    mLoginProgressBar.setVisibility(View.INVISIBLE);
                    mLoginErrorText.setText(mLoginError);
                    mLoginIn = false;
                }

                @Override
                public void onLoginError(String error) {
                    String loginRequestError;

                    switch (error) {

                        case "500:1:Failed to connect to database":
                            loginRequestError = getString(R.string.db_connection_error);
                            break;

                        case "400:1:Bad Input":
                            loginRequestError = getString(R.string.input_field_empty_error);
                            break;

                        case "401:1:Bad user":
                            loginRequestError = getString(R.string.login_wrong_input);
                            break;

                        default:
                            loginRequestError = "Something went wrong, please try again later";
                            Log.wtf(LOG_TAG, "LoginRequest Error: Unknown Error: " + error);
                        }


                    Log.d(LOG_TAG, "Login unsuccessful, Error: " + loginRequestError);
                    mLoginError = loginRequestError;

                    mLoginProgressBar.setVisibility(View.INVISIBLE);
                    mLoginErrorText.setText(mLoginError);
                    mLoginIn = false;
                }
            });

        }else{
            String loginError = getString(R.string.input_field_empty_error);
            Log.d(LOG_TAG, "Login unsuccessful, Error: " + loginError);
            mLoginError = loginError;
            mLoginErrorText.setText(mLoginError);

        }


    }

    private void sendLoginRequest(String loginEmail, String loginPassword, final LoginRequestCallback callback){
        LoginRequest loginRequest = new LoginRequest(loginEmail, loginPassword, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject answer = new JSONObject(response);
                    if(answer.getBoolean("success")){
                        callback.onSuccess(answer);
                    }else{
                        callback.onLoginError(answer.getString("error"));
                    }
                }catch (JSONException e){
                    Log.e(LOG_TAG, "JSON Exception", e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(LOG_TAG,  "LoginRequest Volley Error", error);
            }
        });

        loginRequest.setTag(REQUEST_TAG);
        RequestQueueSingleton.getInstance(this.getContext()).addToRequestQueue(loginRequest);
        Log.d(LOG_TAG,"Start LoginRequest");
    }

    private interface LoginRequestCallback{
        void onSuccess(JSONObject jsonObject);
        void onLoginError(String error);
    }

    public interface onLoginFragmentInteractionListener {
        void onLoginButtonPressed(View v);
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
