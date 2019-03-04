package com.unnamed.studnetz.LoginRegister.register;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.unnamed.studnetz.R;

public class RegisterPasswordFragment extends RegisterChildFragment implements View.OnClickListener, TextWatcher {

    EditText mPasswordField;
    EditText mConfPasswordField;

    Button mNextButton;
    Button mBackButton;

    private int minPasswordLength = 8;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_password, null);

        mPasswordField = view.findViewById(R.id.edittext_register_password);
        mPasswordField.addTextChangedListener(this);
        mConfPasswordField = view.findViewById(R.id.edittextregister_confpassword);
        mConfPasswordField.addTextChangedListener(this);

        // If enter ist pressed on last input field the same happens as if the user pressed the next button
        mConfPasswordField.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN){
                    switch (keyCode){
                        case KeyEvent.KEYCODE_DPAD_DOWN:
                        case KeyEvent.KEYCODE_ENTER:
                            if(checkInput())
                                mListener.nextRegisterChildFragment(new String[]{mPasswordField.getText().toString()});
                            break;
                        default:
                            break;
                    }
                }
                return false;
            }
        });


        mErrorText = view.findViewById(R.id.text_register_password_error);

        mNextButton = view.findViewById(R.id.button_register_password_next);
        mNextButton.setOnClickListener(this);
        mBackButton = view.findViewById(R.id.button_register_password_back);
        mBackButton.setOnClickListener(this);

        updateButton(false);

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

    private void updateButton(boolean enable){
        mNextButton.setEnabled(enable);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_register_password_next:
                mListener.nextRegisterChildFragment(new String[]{mPasswordField.getText().toString()});
                break;
            case R.id.button_register_password_back:
                mListener.backRegisterChildFragment();
                break;
        }
    }

    private boolean checkInput(){

        String password = mPasswordField.getText().toString();
        String confPassword = mConfPasswordField.getText().toString();

        if(isInputEmpty(password, confPassword)){
            setErrorMessage(getString(R.string.input_field_empty_error));
            return false;

        }else if(!isPasswordLongEnough(password)){
            setErrorMessage(getString(R.string.password_to_short_error,minPasswordLength));
            return false;
        }else if(!doPasswordMatch(password, confPassword)){
            setErrorMessage(getString(R.string.password_match_error));
            return false;

        }

        if(isInBlacklist(password)){
            setErrorMessage(getString(R.string.common_password_error));
        }

        mErrorText.setText("");
        return true;

    }

    private boolean isPasswordLongEnough(String input){
        if(input.length() < minPasswordLength){
            return false;
        }
        return  true;
    }

    private boolean isInBlacklist(String input){
        //Check if email is blacklisted
        return false;
    }

    private boolean isInputEmpty(String password, String confPassword){
        return TextUtils.isEmpty(password) || TextUtils.isEmpty(confPassword);
    }

    private boolean doPasswordMatch(String password, String confPassword){
        return password.equals(confPassword);
    }

    @Override
    public void afterTextChanged(Editable s) { updateButton(checkInput()); }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}




}
