package com.unnamed.studnetz.LoginRegister.register;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.unnamed.studnetz.R;
import com.unnamed.studnetz.network.RequestQueueSingleton;
import com.unnamed.studnetz.network.requests.EmailAvailabilityRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterEmailFragment extends RegisterChildFragment implements View.OnClickListener, TextWatcher {

    EditText mEmailTextField;

    Button mNextButton;
    Button mBackButton;

    public static final String REQUEST_TAG = "REGISTER_EMAIL_REQUEST_TAG";
    private RequestQueue mRequestQueue;

    private boolean emailAvailable = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_email, null);

        mErrorText = view.findViewById(R.id.text_register_email_error);

        mEmailTextField = view.findViewById(R.id.edittext_register_email);
        mEmailTextField.addTextChangedListener(this);

        // If enter ist pressed on last input field the same happens as if the user pressed the next button
        mEmailTextField.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN){
                    switch (keyCode){
                        case KeyEvent.KEYCODE_DPAD_DOWN:
                        case KeyEvent.KEYCODE_ENTER:
                            if(checkInput())
                                mListener.nextRegisterChildFragment(new String[]{mEmailTextField.getText().toString()});
                            break;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        mNextButton = view.findViewById(R.id.button_register_email_next);
        mNextButton.setOnClickListener(this);

        mBackButton = view.findViewById(R.id.button_register_email_back);
        mBackButton.setOnClickListener(this);

        updateButton(false);

        mRequestQueue = RequestQueueSingleton.getInstance(getContext()).getRequestQueue();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mErrorText.setText(mErrorMessage);
    }

    @Override
    void setErrorMessage(String errorMessage) {
        mErrorMessage = errorMessage;
        if(mErrorText != null) {
            mErrorText.setText(mErrorMessage);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_register_email_next:
                mListener.nextRegisterChildFragment(new String[]{mEmailTextField.getText().toString()});
                break;

            case R.id.button_register_email_back:
                mListener.backRegisterChildFragment();
                break;
        }
    }

    private boolean checkInput(){

        String input = mEmailTextField.getText().toString();

        if(isInputEmpty(input) ){
            setErrorMessage(getString(R.string.input_field_empty_error));
            return false;
        }else if(!doesEmailMatchPattern(input)){
            setErrorMessage(getString(R.string.valid_email_error));
            return false;
        }else if(!checkEmail(input)){
            setErrorMessage(getString(R.string.email_taken_error));
            return false;
        }

        // If checkEmail isn't last, edit onResponse in checkEmail method


        mErrorText.setText("");
        return true;

    }


    private boolean checkEmail(String input){
        EmailAvailabilityRequest emailRequest = new EmailAvailabilityRequest(input, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject answer = new JSONObject(response);
                    emailAvailable = answer.getBoolean("success");

                    updateButton(emailAvailable);

                    if(emailAvailable) {
                        setErrorMessage("");
                    }

                }catch (JSONException jsone){
                    //Todo: set Error for json parse error
                    setErrorMessage(jsone.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        emailRequest.setTag(REQUEST_TAG);

        RequestQueueSingleton.getInstance(this.getContext()).addToRequestQueue(emailRequest);

        return  emailAvailable;
    }

    private boolean isInputEmpty(String input){
        return TextUtils.isEmpty(input);
    }

    private boolean doesEmailMatchPattern(String input){
        return Patterns.EMAIL_ADDRESS.matcher(input).matches();
    }

    private void updateButton(boolean enable){
        mNextButton.setEnabled(enable);
    }

    @Override
    public void afterTextChanged(Editable s) {
        mNextButton.setEnabled(checkInput());
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void onStop() {
        super.onStop();
        if(mRequestQueue != null){
            mRequestQueue.cancelAll(REQUEST_TAG);
        }
    }
}
