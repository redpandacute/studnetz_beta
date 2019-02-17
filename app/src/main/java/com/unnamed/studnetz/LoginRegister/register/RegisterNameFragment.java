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
import android.widget.TextView;

import com.unnamed.studnetz.R;

public class RegisterNameFragment extends Fragment implements View.OnClickListener, TextWatcher {

    RegisterFragmentChildInteractionListener mListener;
    EditText mFirstNameField;
    EditText mLastNameField;

    Button mNextButton;
    Button mBackButton;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_name, null);

        mFirstNameField = view.findViewById(R.id.edittext_register_firstname);
        mFirstNameField.addTextChangedListener(this);
        mLastNameField = view.findViewById(R.id.edittext_register_lastname);
        mLastNameField.addTextChangedListener(this);

        mNextButton = view.findViewById(R.id.button_register_name_next);
        mNextButton.setOnClickListener(this);
        mBackButton = view.findViewById(R.id.button_register_name_back);
        mBackButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_register_name_next:
                mListener.nextRegisterChildFragment(new String[]{mFirstNameField.getText().toString(),mLastNameField.getText().toString()});
                break;
            case R.id.button_register_name_back:
                mListener.backRegisterChildFragment();
                break;
        }
    }

    private void updateButton(boolean enable){
        mNextButton.setEnabled(enable);
    }

    private boolean checkInput(){

        if(TextUtils.isEmpty(mFirstNameField.getText().toString()) || TextUtils.isEmpty(mLastNameField.getText().toString())){
            return false;
        }else{
            return true;
        }

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
