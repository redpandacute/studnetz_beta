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

public class RegisterPasswordChildFragment extends RegisterChildFragment implements View.OnClickListener, TextWatcher {

    private onRegisterPasswordInteractionListener mListener;

    private static final String LOG_TAG = "RegisterPWCFragment";

    private EditText mPasswordField;
    private EditText mConfPasswordField;

    private Button mNextButton;
    private Button mBackButton;

    private int minPasswordLength = 8;

    private boolean mPasswordFieldFilled= false;
    private boolean mPasswordLongEnough = false;
    private boolean mPasswordsMatch = false;

    private InputMethodManager mInputMethodManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_password, container, false);

        mInputMethodManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);

        mPasswordField = view.findViewById(R.id.edittext_register_password);
        mConfPasswordField = view.findViewById(R.id.edittext_register_confpassword);
        mNextButton = view.findViewById(R.id.button_register_password_next);
        mBackButton = view.findViewById(R.id.button_register_password_back);
        mErrorText = view.findViewById(R.id.text_register_password_error);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mNextButton.setOnClickListener(this);
        mBackButton.setOnClickListener(this);

        mPasswordField.addTextChangedListener(this);
        mConfPasswordField.addTextChangedListener(this);
        mConfPasswordField.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN){
                    switch (keyCode){
                        case KeyEvent.KEYCODE_DPAD_DOWN:
                        case KeyEvent.KEYCODE_ENTER:
                            if(checkInput()) {
                                String password = mPasswordField.getText().toString();
                                mListener.onNextPasswordButtonPressed(password);
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
        if(v.getId() == R.id.button_register_password_next){
            if(checkInput()){
                // Hide soft keyboard
                if(mInputMethodManager != null)
                    mInputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                String password = mPasswordField.getText().toString();
                mListener.onNextPasswordButtonPressed(password);
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

        if(!mPasswordFieldFilled){
            Log.d(LOG_TAG, "Password incorrect, Error: " + getString(R.string.input_field_empty_error));
            setErrorMessage("");
            successful = false;
        }else if(!mPasswordLongEnough){
            Log.d(LOG_TAG, "Password incorrect, Error: " + getString(R.string.password_to_short_error, minPasswordLength));
            setErrorMessage(getString(R.string.password_to_short_error, minPasswordLength));
            successful = false;
        }else if(!mPasswordsMatch){
            Log.d(LOG_TAG, "Password incorrect, Error: " + getString(R.string.password_match_error));
            setErrorMessage(getString(R.string.password_match_error));
            successful = false;
        }

        if(successful) {
            setErrorMessage("");
            Log.d(LOG_TAG, "Password input ok");
        }

        mNextButton.setEnabled(successful);
        return successful;
    }

    // Verifies the input data
    private void verifyInput(){
        String password = mPasswordField.getText().toString();
        String confPassword = mConfPasswordField.getText().toString();

        mPasswordFieldFilled = !TextUtils.isEmpty(password);

        if(mPasswordFieldFilled){
            mPasswordLongEnough = (password.length()>=minPasswordLength);
        }else{
            mPasswordLongEnough = false;
        }

        if(mPasswordFieldFilled && mPasswordLongEnough ){
            mPasswordsMatch = password.equals(confPassword);
        }else{
            mPasswordsMatch = false;
        }

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

    public interface onRegisterPasswordInteractionListener{
        void onNextPasswordButtonPressed(String input);
        void onBackButtonPressed();
    }

    @Override
    public void onStart() {
        super.onStart();
        RegisterFragment parent  = (RegisterFragment) getParentFragment();
        mPasswordField.setText(parent.getFirstName());
        mConfPasswordField.setText(parent.getFirstName());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if( getParentFragment() instanceof onRegisterPasswordInteractionListener){
            mListener = (onRegisterPasswordInteractionListener) getParentFragment();
        }else{
            throw new RuntimeException(context.toString()
                    + "must implement onRegisterPasswordInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
