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

import com.unnamed.studnetz.R;

public class RegisterNameFragment extends RegisterChildFragment implements View.OnClickListener, TextWatcher {

    EditText mFirstNameField;
    EditText mLastNameField;

    Button mNextButton;
    Button mBackButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_name, null);

        mFirstNameField = view.findViewById(R.id.edittext_register_firstname);
        mFirstNameField.addTextChangedListener(this);
        mLastNameField = view.findViewById(R.id.edittext_register_lastname);
        mLastNameField.addTextChangedListener(this);

        // If enter ist pressed on last input field the same happens as if the user pressed the next button
        mLastNameField.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN){
                    switch (keyCode){
                        case KeyEvent.KEYCODE_DPAD_DOWN:
                        case KeyEvent.KEYCODE_ENTER:
                            if(checkInput())
                                mListener.nextRegisterChildFragment(new String[]{mFirstNameField.getText().toString(),mLastNameField.getText().toString()});
                            break;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        mErrorText = view.findViewById(R.id.text_register_name_error);

        mNextButton = view.findViewById(R.id.button_register_name_next);
        mNextButton.setOnClickListener(this);
        mBackButton = view.findViewById(R.id.button_register_name_back);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_register_name_next:
                mListener.nextRegisterChildFragment(new String[]{mFirstNameField.getText().toString(),mLastNameField.getText().toString()});
                break;
        }
    }

    boolean checkInput() {
        if(TextUtils.isEmpty(mFirstNameField.getText().toString()) || TextUtils.isEmpty(mLastNameField.getText().toString())){
            setErrorMessage(getString(R.string.input_field_empty_error));
            return false;
        }else{
            setErrorMessage("");
            return true;
        }
    }

    private void updateButton(boolean enable){
        mNextButton.setEnabled(enable);
    }

    @Override
    public void afterTextChanged(Editable s) {
        updateButton(checkInput());
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }
}
