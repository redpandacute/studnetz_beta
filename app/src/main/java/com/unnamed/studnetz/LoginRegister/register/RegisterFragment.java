package com.unnamed.studnetz.LoginRegister.register;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.unnamed.studnetz.LoginRegister.ChangeLoginRegisterFragment;
import com.unnamed.studnetz.LoginRegister.LoginRegisterActivity;
import com.unnamed.studnetz.LoginRegister.login.LoginFragment;
import com.unnamed.studnetz.R;

public class RegisterFragment extends Fragment implements View.OnClickListener {

    LoginRegisterActivity mChangeFragment;



    TextView mRegisterSignIn;

    Button mNextButton;
    Button mBackButton;

    private int mActiveRegisterFragment;
    private Fragment[] mRegisterFragmentList = new Fragment[]{new FragmentRegisterEmail(),new FragmentRegisterPassword(), new FragmentRegisterName()};

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, null);

        mNextButton = view.findViewById(R.id.register_signup_next_button);
        mNextButton.setOnClickListener(this);
        mBackButton = view.findViewById(R.id.register_signup_back_button);
        mBackButton.setOnClickListener(this);

        replaceChildFragment(mRegisterFragmentList[0], false);

        mRegisterSignIn = view.findViewById(R.id.register_signin);
        mRegisterSignIn.setOnClickListener(this);


        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof ChangeLoginRegisterFragment){
            mChangeFragment = (LoginRegisterActivity) context;
        }else{
            throw new RuntimeException(context.toString() + " must implement ChangeLoginRegisterFragment");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mChangeFragment = null;
    }

    private boolean replaceChildFragment(Fragment fragment, boolean addToBackStack){
        if(fragment == null){
            return false;
        }
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_register_container, fragment);
        if(addToBackStack)
            fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        return true;
    }

    private Fragment getNextFragment(boolean next){
        Fragment nextFragment;

        if(next){

            if(mActiveRegisterFragment == mRegisterFragmentList.length-1){
                nextFragment = null;
            }else{
                mActiveRegisterFragment++;
                nextFragment = mRegisterFragmentList[mActiveRegisterFragment];
            }

        }else{
            if(mActiveRegisterFragment == 0){
                nextFragment = null;
            }else{
                mActiveRegisterFragment--;
                nextFragment = mRegisterFragmentList[mActiveRegisterFragment];
            }
        }

        return nextFragment;
    }

    private void updateButtons(){
        if(mActiveRegisterFragment == 0){
            mBackButton.setVisibility(View.INVISIBLE);
        }else if(mBackButton.getVisibility() == View.INVISIBLE){
            mBackButton.setVisibility(View.VISIBLE);
        }

        if(mActiveRegisterFragment == mRegisterFragmentList.length-1){
            mNextButton.setText(R.string.send);
        }else{
            mNextButton.setText(R.string.next);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register_signin:
                mChangeFragment.onLoginSelected();
                break;

            case R.id.register_signup_next_button:
                if(!(mActiveRegisterFragment == mRegisterFragmentList.length-1)) {
                replaceChildFragment(getNextFragment(true), true);
                updateButtons();
                }else{

                }
                break;

            case R.id.register_signup_back_button:
                replaceChildFragment(getNextFragment(false), true);
                updateButtons();
                break;
        }
    }

    public interface RegisterFragmentInterface {
        public void onRegistration(); //Example
        public void onLoginSelected();
    }
}
