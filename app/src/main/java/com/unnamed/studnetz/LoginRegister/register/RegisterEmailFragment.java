package com.unnamed.studnetz.LoginRegister.register;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

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
import android.widget.TextView;

import com.unnamed.studnetz.R;

public class RegisterEmailFragment extends Fragment implements View.OnClickListener{

    TextView mErrorText;
    EditText mEmailTextField;
    Button mNextButton;

    RegisterFragmentChildInteractionListener mListener;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_email, null);

        mErrorText = view.findViewById(R.id.text_register_email_error);

        mEmailTextField = view.findViewById(R.id.edittext_register_email);
        mEmailTextField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) { mNextButton.setEnabled(checkInput());}
        });

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

        updateButton(false);

        return view;
    }

    private void updateButton(boolean enable){
        mNextButton.setEnabled(enable);
    }


    // Checks input and sets the error message
    private boolean checkInput(){

        String input = mEmailTextField.getText().toString();

        if(isInputEmpty(input) ){
            mErrorText.setText(R.string.input_field_empty_error);
            return false;
        }else if(!doesEmailMatchPattern(input)){
            mErrorText.setText(R.string.valid_email_error);
            return false;
        }else if(isEmailAlreadyTaken(input)){
            mErrorText.setText(R.string.email_taken_error);
            return false;
        }

        mErrorText.setText("");
        return true;

    }

    private boolean isEmailAlreadyTaken(String input){
        //Check if email has already been taken
        return false;
    }

    private boolean isInputEmpty(String input){
        return TextUtils.isEmpty(input);
    }

    private boolean doesEmailMatchPattern(String input){
        return Patterns.EMAIL_ADDRESS.matcher(input).matches();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_register_email_next:
                mListener.nextRegisterChildFragment(new String[]{mEmailTextField.getText().toString()});
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if( context instanceof RegisterFragmentChildInteractionListener){
            mListener = (RegisterFragmentChildInteractionListener) context;
        }else{
            throw new RuntimeException(context.toString()
                    + " must implement RegisterFragmentChildInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
