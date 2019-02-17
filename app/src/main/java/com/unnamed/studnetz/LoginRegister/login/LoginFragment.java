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
import android.widget.Toast;

import com.unnamed.studnetz.LoginRegister.register.RegisterFragment;
import com.unnamed.studnetz.R;

public class LoginFragment extends Fragment implements View.OnClickListener {

    public interface onLoginFragmentInteractionListener {
        void onLoginButtonPressed(View v);
    }

    private onLoginFragmentInteractionListener mListener;

    ProgressBar mLoginProgressBar;

    private TextView mLoginErrorText;
    private EditText mPasswordField;
    private EditText mEmailField;



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

        return view;

    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.button_login_signin){
            Toast.makeText(getContext(), "SignIn", Toast.LENGTH_SHORT).show();
            login();
        }else{
           mListener.onLoginButtonPressed(v);
        }

    }

    private void login()  {
        String mLoginEmail = mEmailField.getText().toString();
        String mLoginPassword = mPasswordField.getText().toString();

        if(!TextUtils.isEmpty(mLoginEmail) && !TextUtils.isEmpty(mLoginPassword)){

            mLoginProgressBar.setVisibility(View.VISIBLE);

            //TODO: Sign in


        }else{
            mLoginErrorText.setText(R.string.login_empty_field_error);
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
}
