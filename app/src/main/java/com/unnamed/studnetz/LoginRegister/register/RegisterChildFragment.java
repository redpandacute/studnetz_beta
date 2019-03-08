package com.unnamed.studnetz.LoginRegister.register;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.TextView;

public abstract class RegisterChildFragment extends Fragment {

    TextView mErrorText;
    String mErrorMessage = "";

    abstract void setErrorMessage(String errorMessage);
    abstract boolean checkInput();

}
