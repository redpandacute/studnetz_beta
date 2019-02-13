package com.unnamed.studnetz.LoginRegister.login;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.unnamed.studnetz.LoginRegister.ChangeLoginRegisterFragment;
import com.unnamed.studnetz.R;
import com.unnamed.studnetz.main.fragments.HomeFragment;

public class LoginFragment extends Fragment implements View.OnClickListener {

    ChangeLoginRegisterFragment mChangeFragment;

    Button mLoginButton;

    TextView mLoginSignUp;
    TextView mLoginSignUpNow;
    TextView mLoginErrorText;

    TextView mForgotPassword;

    EditText mPasswordField;
    EditText mEmailField;

    ProgressBar mLoginProgressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, null);

        mLoginProgressBar = view.findViewById(R.id.login_progressBar);

        mPasswordField = view.findViewById(R.id.login_password_edittext);
        mEmailField = view.findViewById(R.id.login_email_edittext);

        mLoginErrorText = view.findViewById(R.id.login_error_text);
        mLoginSignUp = view.findViewById(R.id.login_signup);
        mLoginSignUp.setOnClickListener(this);
        mLoginSignUpNow = view.findViewById(R.id.login_signupnow);
        mLoginSignUpNow.setOnClickListener(this);
        mForgotPassword= view.findViewById(R.id.login_forgot_password);
        mForgotPassword.setOnClickListener(this);

        mLoginButton = view.findViewById(R.id.login_signin_button);
        mLoginButton.setOnClickListener(this);

        return view;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof ChangeLoginRegisterFragment){
            mChangeFragment = (ChangeLoginRegisterFragment) context;
        }else{
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mChangeFragment = null;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.login_signin_button:
                Toast.makeText(getContext(), "SignIn", Toast.LENGTH_SHORT).show();
                login();
                break;

            case R.id.login_forgot_password:
                Toast.makeText(getContext(), "open forgot Password", Toast.LENGTH_SHORT).show();
                break;

            case R.id.login_signup:
            case R.id.login_signupnow:
                mChangeFragment.ChangeFragment(R.id.login_signup);
                break;
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
}
