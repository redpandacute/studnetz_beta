package com.unnamed.studnetz.LoginRegister.register;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.unnamed.studnetz.R;

import org.w3c.dom.Text;

public class RegisterPasswordFragment extends Fragment implements View.OnClickListener, TextWatcher {

    RegisterFragmentChildInteractionListener mListener;
    EditText mPasswordField;
    EditText mConfPasswordField;

    TextView mErrorText;

    Button mNextButton;
    Button mBackButton;

    private int minPasswordLength = 8;

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

        return view;
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

    private void updateButton(boolean enable){
        mNextButton.setEnabled(enable);
    }

    private boolean checkInput(){

        String password = mPasswordField.getText().toString();
        String confPassword = mConfPasswordField.getText().toString();

        if(isInputEmpty(password, confPassword)){
            mErrorText.setText(R.string.input_field_empty_error);
            return false;

        }else if(!isPasswordLongEnough(password)){
            mErrorText.setText(getString(R.string.password_to_short_error,minPasswordLength));
            return false;
        }else if(!doPasswordMatch(password, confPassword)){
            mErrorText.setText(R.string.password_match_error);
            return false;

        }

        if(isInBlacklist(password)){
            mErrorText.setText(R.string.common_password_error);
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
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        updateButton(checkInput());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if( context instanceof RegisterFragmentChildInteractionListener){
            mListener = (RegisterFragmentChildInteractionListener) context;
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
}
