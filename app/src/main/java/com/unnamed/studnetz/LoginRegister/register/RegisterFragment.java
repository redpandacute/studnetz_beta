package com.unnamed.studnetz.LoginRegister.register;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.unnamed.studnetz.LoginRegister.LoginRegisterActivity;
import com.unnamed.studnetz.LoginRegister.login.LoginFragment;
import com.unnamed.studnetz.R;

public class RegisterFragment extends Fragment implements View.OnClickListener{

    public interface onRegisterFragmentInteractionListener {
        void onRegisterButtonPressed(View v);
    }

    private onRegisterFragmentInteractionListener mListener;

    FragmentManager cfm;

    TextView mButtonLogin;

    private int mActiveRegisterFragment;
    private Fragment[] mRegisterFragmentList = new Fragment[]{new RegisterEmailFragment(),new RegisterPasswordFragment(), new RegisterNameFragment()};

    private String mEmail;
    private String mPassword;
    private String mFirstName;
    private String mLastName;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, null);

        cfm = getChildFragmentManager();

        mButtonLogin = view.findViewById(R.id.register_login);
        mButtonLogin.setOnClickListener(this);

        if(savedInstanceState == null) {
            FragmentTransaction fragmentTransaction = cfm.beginTransaction();
            fragmentTransaction.add(R.id.flcontainer_register_fragment, mRegisterFragmentList[0]);
            fragmentTransaction.commitNow();
        }

        return view;
    }

    @Override
    public void onClick(View v) {
        mListener.onRegisterButtonPressed(v);
    }


    public void nextFragment(String[] data) {
        if(mActiveRegisterFragment>=mRegisterFragmentList.length){
            mActiveRegisterFragment = mRegisterFragmentList.length-1;
        }else if(mActiveRegisterFragment < 0){
            mActiveRegisterFragment = 0;
        }

        saveData(data);

        if(mActiveRegisterFragment == mRegisterFragmentList.length-1){
            // Send information, Complete registration process
            Toast.makeText(getContext(),"DATA: Email: " + mEmail + " Password: " + mPassword + " Name: " + mFirstName + " " + mLastName, Toast.LENGTH_LONG).show();
        }else{
            mActiveRegisterFragment++;
            replaceFragment(mRegisterFragmentList[mActiveRegisterFragment],true);
        }
    }

    public void saveData(String[] data){
        if(mActiveRegisterFragment==0) {
            mEmail = data[0];
        }else if(mActiveRegisterFragment==1){
            mPassword = data[0];
        }else if(mActiveRegisterFragment==2){
            mFirstName = data[0];
            mLastName = data[1];
        }
    }

    //TODO: modify backStack when going back


    public void backFragment() {
        if(mActiveRegisterFragment>=mRegisterFragmentList.length){
            mActiveRegisterFragment = mRegisterFragmentList.length-1;
        }else if(mActiveRegisterFragment < 0){
            mActiveRegisterFragment = 0;
        }

        if(mActiveRegisterFragment != 0){
            mActiveRegisterFragment--;
            replaceFragment(mRegisterFragmentList[mActiveRegisterFragment],false);
        }
    }

    private void replaceFragment(Fragment fragment, boolean addToBackStack){
        if(fragment == null){
            return;
        }
        FragmentTransaction fragmentTransaction = cfm.beginTransaction();
        fragmentTransaction.replace(R.id.flcontainer_register_fragment, fragment);

        if(addToBackStack)
            fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();
    }

    private void replaceFragment(Fragment fragment, boolean addToBackStack, String clearBackStackUpTo){
        cfm.popBackStack(clearBackStackUpTo ,FragmentManager.POP_BACK_STACK_INCLUSIVE);

        replaceFragment(fragment,addToBackStack);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if( context instanceof onRegisterFragmentInteractionListener){
            mListener = (onRegisterFragmentInteractionListener) context;
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
