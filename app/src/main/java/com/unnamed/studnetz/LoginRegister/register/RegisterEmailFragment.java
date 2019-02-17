package com.unnamed.studnetz.LoginRegister.register;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.unnamed.studnetz.R;

public class RegisterEmailFragment extends Fragment implements View.OnClickListener{

    EditText mEmailTextField;
    Button mNextButton;

    RegisterFragmentChildInteractionListener mListener;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_email, null);

        mEmailTextField = view.findViewById(R.id.edittext_register_email);
        mEmailTextField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) { mNextButton.setEnabled(checkInput());}
        });

        mNextButton = view.findViewById(R.id.button_register_email_next);
        mNextButton.setOnClickListener(this);

        updateButton(false);

        return view;
    }

    private void updateButton(boolean enable){
        mNextButton.setEnabled(enable);
    }

    private boolean checkInput(){

        if(TextUtils.isEmpty(mEmailTextField.getText().toString())){

            return false;

        }else{

            return true;

        }

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
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
