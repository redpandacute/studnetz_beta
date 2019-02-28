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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.unnamed.studnetz.LoginRegister.LoginRegisterManager;
import com.unnamed.studnetz.LoginRegister.login.LoginFragment;
import com.unnamed.studnetz.R;
import com.unnamed.studnetz.network.RequestQueueSingleton;
import com.unnamed.studnetz.network.requests.RegisterRequest;

public class RegisterFragment extends Fragment implements View.OnClickListener{


    public static final String REQUEST_TAG = "RegisterTag";

    public interface onRegisterFragmentInteractionListener {
        void onRegisterButtonPressed(View v);
    }

    private onRegisterFragmentInteractionListener mListener;

    FragmentManager cfm;

    TextView mButtonLogin;
    ProgressBar mRegisterProgressBar;

    private int mActiveRegisterFragment;

    // List of Fragments that show during registration progress sequentially
    // !!!IMPORTANT!!! Fragment sequence has to match saveData() method
    private static final Fragment[] REGISTER_FRAGMENT_LIST = new Fragment[]{new RegisterNameFragment(),new RegisterEmailFragment(),new RegisterPasswordFragment()};

    private String mEmail;
    private String mPassword;
    private String mFirstName;
    private String mLastName;

    private RequestQueue mRequestQueue;

    private boolean register = false;

    LoginRegisterManager mLRManager;

    public static RegisterFragment newInstance(LoginRegisterManager lRManager){
        RegisterFragment fragment = new RegisterFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("LoginRegisterManager", lRManager);
        fragment.setArguments(bundle);

        return fragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, null);

        mLRManager = (LoginRegisterManager) getArguments().getSerializable("LoginRegisterManager");

        cfm = getChildFragmentManager();

        mButtonLogin = view.findViewById(R.id.register_login);
        mButtonLogin.setOnClickListener(this);
        mRegisterProgressBar = view.findViewById(R.id.register_progressBar);

        if(savedInstanceState == null) {
            FragmentTransaction fragmentTransaction = cfm.beginTransaction();
            fragmentTransaction.add(R.id.flcontainer_register_fragment, REGISTER_FRAGMENT_LIST[0]);
            fragmentTransaction.commitNow();
        }

        mActiveRegisterFragment = 0;


        mRequestQueue = RequestQueueSingleton.getInstance(view.getContext()).getRequestQueue();

        return view;
    }

    @Override
    public void onClick(View v) {
        if(!register)
        mListener.onRegisterButtonPressed(v);
    }


    public void nextFragment(String[] data) {
        if(mActiveRegisterFragment>= REGISTER_FRAGMENT_LIST.length){
            mActiveRegisterFragment = REGISTER_FRAGMENT_LIST.length-1;
        }else if(mActiveRegisterFragment < 0){
            mActiveRegisterFragment = 0;
        }

        saveData(data);

        if(mActiveRegisterFragment == REGISTER_FRAGMENT_LIST.length-1){
            // Send information, Complete registration process
            register = true;
            Toast.makeText(getContext(),"DATA: " + " Name: " + mFirstName + " " + mLastName + " Email: " + mEmail + " Password: " + mPassword , Toast.LENGTH_LONG).show();
            register();
        }else{
            mActiveRegisterFragment++;
            replaceFragment(REGISTER_FRAGMENT_LIST[mActiveRegisterFragment],true);
        }
    }

    private String register(){
        String error = "";
        mRegisterProgressBar.setVisibility(View.VISIBLE);

        RegisterRequest registerRequest = new RegisterRequest(mEmail, mFirstName,mLastName,mPassword, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getContext(),response, Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                //TODO: Error handling
                Toast.makeText(getContext(),error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        registerRequest.setTag(REQUEST_TAG);

        RequestQueueSingleton.getInstance(this.getContext()).addToRequestQueue(registerRequest);

        mRegisterProgressBar.setVisibility(View.INVISIBLE);
        register = false;
        return error;
    }

    /*
    Save Data from Fragments in Variables
     */
    public void saveData(String[] data){
        if(mActiveRegisterFragment==0) {
            mFirstName = data[0];
            mLastName = data[1];
        }else if(mActiveRegisterFragment==1){
            mEmail = data[0];
        }else if(mActiveRegisterFragment==2){
            mPassword = data[0];
        }
    }

    //TODO: modify backStack when going back

    public void backFragment() {
        if(mActiveRegisterFragment>= REGISTER_FRAGMENT_LIST.length){
            mActiveRegisterFragment = REGISTER_FRAGMENT_LIST.length-1;
        }else if(mActiveRegisterFragment < 0){
            mActiveRegisterFragment = 0;
        }

        if(mActiveRegisterFragment != 0){
            mActiveRegisterFragment--;
            replaceFragment(REGISTER_FRAGMENT_LIST[mActiveRegisterFragment],false);
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

    @Override
    public void onStop() {
        super.onStop();
        if(mRequestQueue != null){
            mRequestQueue.cancelAll(REQUEST_TAG);
        }
    }

}
