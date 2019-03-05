package com.unnamed.studnetz.LoginRegister.register;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.TextView;

public abstract class RegisterChildFragment extends Fragment {

    RegisterFragmentChildInteractionListener mListener;

    TextView mErrorText;
    String mErrorMessage = "";

    abstract void setErrorMessage(String errorMessage);

    private boolean checkInput(){
        return false;
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if( context instanceof RegisterFragmentChildInteractionListener){
            mListener = (RegisterFragmentChildInteractionListener) context;
        }else{
            throw new RuntimeException(context.toString()
                    + "must implement RegisterFragmentChildInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
