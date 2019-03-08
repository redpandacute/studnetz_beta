package com.unnamed.studnetz.LoginRegister.register;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.unnamed.studnetz.R;

public class RegisterNameChildFragment extends RegisterChildFragment implements View.OnClickListener, TextWatcher {

    private onRegisterNameInteractionListener mListener;

    private static final String LOG_TAG = "RegisterPWCFragment";

    private EditText mFirstNameField;
    private EditText mLastNameField;

    private Button mNextButton;
    private Button mBackButton;

    private boolean mNameFieldsFilled= false;

    private InputMethodManager mInputMethodManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_name, container, false);

        mInputMethodManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);

        mFirstNameField = view.findViewById(R.id.edittext_register_firstname);
        mLastNameField = view.findViewById(R.id.edittext_register_lastname);
        mNextButton = view.findViewById(R.id.button_register_name_next);
        mBackButton = view.findViewById(R.id.button_register_name_back);
        mErrorText = view.findViewById(R.id.text_register_name_error);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mNextButton.setOnClickListener(this);
        mBackButton.setOnClickListener(this);

        mFirstNameField.addTextChangedListener(this);
        mLastNameField.addTextChangedListener(this);
        mLastNameField.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN){
                    switch (keyCode){
                        case KeyEvent.KEYCODE_DPAD_DOWN:
                        case KeyEvent.KEYCODE_ENTER:
                            if(checkInput()) {
                                String firstName = mFirstNameField.getText().toString();
                                String lastName = mLastNameField.getText().toString();
                                mListener.onNextNameButtonPressed(firstName,lastName);
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
    public void onClick(View v) {
        if(v.getId() == R.id.button_register_name_next){
            if(checkInput()){
                // Hide soft keyboard
                if(mInputMethodManager != null)
                    mInputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                String firstName = mFirstNameField.getText().toString();
                String lastName = mLastNameField.getText().toString();
                mListener.onNextNameButtonPressed(firstName,lastName);
            }
        }else if(v.getId() == R.id.button_register_password_back){
            mListener.onBackButtonPressed();
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

        if(!mNameFieldsFilled){
            Log.d(LOG_TAG, "Name incorrect, Error: " + getString(R.string.input_field_empty_error));
            setErrorMessage("");
            successful = false;
        }

        if(successful) {
            setErrorMessage("");
            Log.d(LOG_TAG, "Name input ok");
        }

        mNextButton.setEnabled(successful);
        return successful;
    }

    // Verifies the input data
    private void verifyInput(){
        String firstName = mFirstNameField.getText().toString();
        String lastName = mLastNameField.getText().toString();

        mNameFieldsFilled = !TextUtils.isEmpty(firstName) && !TextUtils.isEmpty(lastName);

    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable s) {
        verifyInput();
        checkInput();
    }

    public interface onRegisterNameInteractionListener{
        void onNextNameButtonPressed(String firstName, String lastName);
        void onBackButtonPressed();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Resets text in input fields
        RegisterFragment parent  = (RegisterFragment) getParentFragment();
        mFirstNameField.setText(parent.getFirstName());
        mLastNameField.setText(parent.getLastName());
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if( getParentFragment() instanceof onRegisterNameInteractionListener){
            mListener = (onRegisterNameInteractionListener) getParentFragment();
        }else{
            throw new RuntimeException(context.toString()
                    + "must implement onRegisterNameInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



}
