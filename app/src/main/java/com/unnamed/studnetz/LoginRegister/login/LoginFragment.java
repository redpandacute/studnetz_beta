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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.unnamed.studnetz.R;
import com.unnamed.studnetz.network.RequestQueueSingleton;
import com.unnamed.studnetz.network.requests.LoginRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginFragment extends Fragment implements View.OnClickListener {

    public interface onLoginFragmentInteractionListener {
        void onLoginButtonPressed(View v);
    }


    public static final String REQUEST_TAG = "LOGIN_REQUEST_TAG";

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

        mRequestQueue = RequestQueueSingleton.getInstance(getContext()).getRequestQueue();

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

        String loginEmail = mEmailField.getText().toString();
        String loginPassword = mPasswordField.getText().toString();

        if(!TextUtils.isEmpty(loginEmail) && !TextUtils.isEmpty(loginPassword)){

            loginIn = true;
            mLoginProgressBar.setVisibility(View.VISIBLE);

            Map<String, String> input = new HashMap<>();
            input.put("email",loginEmail);
            input.put("password",loginPassword);

            LoginRequest loginRequest = new LoginRequest(input, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    String error = "";

                    //FIXME: Server return json and error back
                    String answer = response.substring(0,response.lastIndexOf('}')+1);

                    try {
                        JSONObject jsonAnswer = new JSONObject(answer);

                        boolean success = jsonAnswer.getBoolean("success");
                        if(success){
                            //TODO: if login successful set user
                            JSONObject user = jsonAnswer.getJSONObject("user");
                            error = "Hello " + user.getString("firstname") + " " + user.getString("lastname");
                        }else{
                            //if login not successful get error
                            String answerError = jsonAnswer.getString("error");

                            switch (answerError){
                                case "400:1:Bad Input":
                                    error = getContext().getResources().getString(R.string.input_field_empty_error);
                                    break;

                                case"404:1:User not found":
                                    error = getContext().getResources().getString(R.string.login_wrong_input);
                                    break;

                                case"401:1:Bad Password":
                                    error = getContext().getResources().getString(R.string.login_wrong_input);
                                    break;

                            }

                        }

                    }catch (JSONException jsone){
                        //Todo: set Answer for json parse error
                        error = "";
                    }

                    mLoginProgressBar.setVisibility(View.INVISIBLE);
                    mLoginErrorText.setText(error);
                    loginIn = false;
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    //TODO: Error handling
                    mLoginProgressBar.setVisibility(View.INVISIBLE);
                    mLoginErrorText.setText(error.toString());
                    loginIn = false;
                }
            });

            loginRequest.setTag(REQUEST_TAG);

            RequestQueueSingleton.getInstance(this.getContext()).addToRequestQueue(loginRequest);

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
