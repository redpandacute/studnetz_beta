package com.unnamed.studnetz.LoginRegister.register;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.unnamed.studnetz.R;
import com.unnamed.studnetz.network.RequestQueueSingleton;
import com.unnamed.studnetz.network.requests.EmailAvailabilityRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterEmailChildFragment extends RegisterChildFragment implements View.OnClickListener, TextWatcher {

    private onRegisterEmailInteractionListener mListener;

    private static final String REQUEST_TAG = "REGISTER_EMAIL_REQUEST_TAG";
    private static final String LOG_TAG = "RegisterEmailCFragment";

    private EditText mEmailTextField;
    private Button mNextButton;

    private boolean mEmailFieldFilled = false;
    private boolean mEmailMatchesPattern = false;
    private boolean mEmailAvailable = false;

    private RequestQueue mRequestQueue;

    private InputMethodManager mInputMethodManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_email, container, false);

        mInputMethodManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);

        mRequestQueue = RequestQueueSingleton.getInstance(getContext()).getRequestQueue();

        mEmailTextField = view.findViewById(R.id.edittext_register_email);

        mNextButton = view.findViewById(R.id.button_register_email_next);
        mErrorText = view.findViewById(R.id.text_register_email_error);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mNextButton.setOnClickListener(this);
        mEmailTextField.addTextChangedListener(this);
        mEmailTextField.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN){
                    switch (keyCode){
                        case KeyEvent.KEYCODE_DPAD_DOWN:
                        case KeyEvent.KEYCODE_ENTER:
                            if(checkInput()) {
                                String email = mEmailTextField.getText().toString();
                                mListener.onNextEmailButtonPressed(email);
                            }
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
    public void onStart() {
        super.onStart();

        // Resets text in input fields
        RegisterFragment parent  = (RegisterFragment) getParentFragment();
        mEmailTextField.setText(parent.getEmail());
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.button_register_email_next){
            if(checkInput()){
                // Hide soft keyboard
                if(mInputMethodManager != null)
                    mInputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                String email = mEmailTextField.getText().toString();
                mListener.onNextEmailButtonPressed(email);
            }
        }
    }

    @Override
    public void setErrorMessage(String errorMessage) {
        mErrorMessage = errorMessage;
        updateErrorField();
    }

    private void updateErrorField(){
        mErrorText.setText(mErrorMessage);
    }

    // Sets Errors and manages the next button
    // verifyInput() needs too be called first, to update requirements
    @Override
    boolean checkInput() {
        boolean successful = true;

        if(!mEmailFieldFilled){
            Log.d(LOG_TAG, "Email incorrect, Error: " + getString(R.string.input_field_empty_error));
            setErrorMessage("");
            successful = false;
        }else if(!mEmailMatchesPattern){
            Log.d(LOG_TAG, "Email incorrect, Error: " + getString(R.string.valid_email_error));
            setErrorMessage(getString(R.string.valid_email_error));
            successful = false;
        }else if(!mEmailAvailable){
            Log.d(LOG_TAG, "Email incorrect, Error: " + getString(R.string.email_taken_error));
            setErrorMessage(getString(R.string.email_taken_error));
            successful = false;
        }

        if(successful) {
            setErrorMessage("");
            Log.d(LOG_TAG, "Email input ok");
        }

        mNextButton.setEnabled(successful);
        return successful;
    }

    // Verifies the input data
    private void verifyInput(){
        String email = mEmailTextField.getText().toString();

        mEmailFieldFilled = !TextUtils.isEmpty(email);

        if(mEmailFieldFilled){
            mEmailMatchesPattern = checkPattern(email);
        }else{
            mEmailMatchesPattern = false;
        }

        if(mEmailFieldFilled && mEmailMatchesPattern ){
            emailAvailableRequest(email, new EmailAvailabilityRequestCallback() {
                @Override
                public void onSuccess() {
                    mEmailAvailable = true;
                    Log.d(LOG_TAG, "Email available");
                    checkInput();
                }

                @Override
                public void onLoginError(String error) {
                    mEmailAvailable = false;

                    String loginRequestError = "";

                    switch (error) {

                        case "500:1:Failed to connect to database":
                            loginRequestError = getString(R.string.db_connection_error);
                            break;

                        case "400:1:Bad Input":
                            Log.d(LOG_TAG, "Server was unable to retrieve data");
                            break;

                        case "406:1:Email taken":
                            loginRequestError = getString(R.string.email_taken_error);
                            break;

                        default:
                             loginRequestError = "Something went wrong, please try again later";
                             Log.wtf(LOG_TAG, "EmailAvailabilityRequest Error: Unknown Error: " + error);
                    }

                    Log.d(LOG_TAG, "Email not available, Error: " + loginRequestError);
                    checkInput();
                }
            });

        }else{
            mEmailAvailable = false;
        }

    }

    private boolean checkPattern(String input){
        Pattern emailPattern = Patterns.EMAIL_ADDRESS;
        Matcher emailMatcher = emailPattern.matcher(input);
        return emailMatcher.matches();
    }

    private void emailAvailableRequest(String input , final EmailAvailabilityRequestCallback callback){

        EmailAvailabilityRequest emailAvailabilityRequest = new EmailAvailabilityRequest(input, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject answer = new JSONObject(response);
                    if(answer.getBoolean("success")){
                        callback.onSuccess();
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
                Log.e(LOG_TAG,  "emailAvailabilityRequest Volley Error", error);
            }
        });

        emailAvailabilityRequest.setTag(REQUEST_TAG);

        RequestQueueSingleton.getInstance(this.getContext()).addToRequestQueue(emailAvailabilityRequest);
        Log.d(LOG_TAG,"Start EmailAvailabilityRequest");
    }

    private interface EmailAvailabilityRequestCallback{
        void onSuccess();
        void onLoginError(String error);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        verifyInput();
        checkInput();
    }

    public interface onRegisterEmailInteractionListener{
        void onNextEmailButtonPressed(String input);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if( getParentFragment() instanceof onRegisterEmailInteractionListener){
            mListener = (onRegisterEmailInteractionListener) getParentFragment();
        }else{
            throw new RuntimeException(context.toString()
                    + "must implement onRegisterEmailInteractionListener");
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
