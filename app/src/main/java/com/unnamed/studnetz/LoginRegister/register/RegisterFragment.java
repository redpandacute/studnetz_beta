package com.unnamed.studnetz.LoginRegister.register;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.unnamed.studnetz.R;
import com.unnamed.studnetz.network.ManagerSingleton;
import com.unnamed.studnetz.network.requests.RegisterRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RegisterFragment extends Fragment implements RegisterEmailChildFragment.onRegisterEmailInteractionListener,RegisterPasswordChildFragment.onRegisterPasswordInteractionListener, RegisterNameChildFragment.onRegisterNameInteractionListener, View.OnClickListener{

    private static final String REQUEST_TAG = "RegisterTag";
    private static final String LOG_TAG = "RegisterFragment";

    public interface onRegisterFragmentInteractionListener {
        void onRegisterButtonPressed(View v);
        void onRegisterProcessComplete();
    }

    private onRegisterFragmentInteractionListener mListener;

    private FragmentManager cfm;

    private TextView mGoToLoginButton;
    private ProgressBar mRegisterProgressBar;

    private RequestQueue mRequestQueue;

    private RegisterChildFragment[] mRegisterChildFragmentList = new RegisterChildFragment[]{new RegisterEmailChildFragment(), new RegisterPasswordChildFragment(), new RegisterNameChildFragment()};
    private int mActiveFragment = 0;

    private boolean mRegister = false;

    private String mEmail = "";
    private String mPassword = "";
    private String mFirstName = "";
    private String mLastName = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        cfm = getChildFragmentManager();

        if(savedInstanceState == null){
            FragmentTransaction fragmentTransaction = cfm.beginTransaction();
            fragmentTransaction.add(R.id.flcontainer_register_fragment, mRegisterChildFragmentList[0]);
            fragmentTransaction.commitNow();
        }

        mRequestQueue = ManagerSingleton.getInstance(view.getContext()).getRequestQueue();

        mGoToLoginButton = view.findViewById(R.id.register_login);
        mRegisterProgressBar = view.findViewById(R.id.register_progressBar);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mGoToLoginButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.register_login) {
            if(!mRegister) {
                mActiveFragment = 0;

                // Reset Data if user returns back to Login Fragment
                mEmail = "";
                mPassword = "";
                mLastName = "";
                mFirstName = "";

                mListener.onRegisterButtonPressed(v);

                // If the RegisterChildFragment doesn't match the mActive Fragment change it
                if(mRegisterChildFragmentList[mActiveFragment] != cfm.getFragments().get(0)){
                    replaceFragment(mRegisterChildFragmentList[mActiveFragment],false);
                }
            }
        }
    }

    private void replaceFragment(RegisterChildFragment fragment, boolean addToBackStack){
        Log.d(LOG_TAG,"Replacing RegisterChildFragment with " + fragment.toString());

        FragmentTransaction fragmentTransaction = cfm.beginTransaction();
        fragmentTransaction.replace(R.id.flcontainer_register_fragment, fragment);

        if(addToBackStack)
            fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();
    }

    // Determines the next fragment to be displayed
    private void nextChildFragment(){
        if(mActiveFragment > mRegisterChildFragmentList.length-1){
            mActiveFragment = mRegisterChildFragmentList.length-1;
        }else if(mActiveFragment < 0){
            mActiveFragment = 0;
        }

        if(mActiveFragment == mRegisterChildFragmentList.length-1){
            Log.d(LOG_TAG,"Start register process");
            if(!mRegister)
                register();
        }else{
            mActiveFragment++;
            replaceFragment(mRegisterChildFragmentList[mActiveFragment], true);
        }
    }

    // Register process with error handling
    private void register(){
        if(TextUtils.isEmpty(mEmail) || TextUtils.isEmpty(mPassword) || TextUtils.isEmpty(mFirstName) || TextUtils.isEmpty(mLastName)) {
            Log.wtf(LOG_TAG, "Register Error: Data is empty");
            return;
        }
        mRegister = true;

        sendRegisterRequest(mEmail, mPassword, mFirstName, mLastName, new RegisterRequestCallback() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                // Todo: Set active User
                Log.d(LOG_TAG, "Register successful, User:" + jsonObject.toString());

                for (RegisterChildFragment fragment: mRegisterChildFragmentList) {
                    fragment.setErrorMessage("");
                }
                mRegisterProgressBar.setVisibility(View.INVISIBLE);
                mRegister = false;
            }

            @Override
            public void onLoginError(JSONArray errors) {

                StringBuilder sb = new StringBuilder();


                for (int i = 0; i < errors.length(); i++) {

                    try {

                        String registerRequestError;
                        String errorPosition = "";
                        String error = errors.getString(i);

                        switch (error) {

                            case "406:1:bad firstname":
                                errorPosition = "firstname";
                                registerRequestError = getString(R.string.input_field_empty_error);
                                break;

                            case "406:2:bad lastname;":
                                errorPosition = "lastname";
                                registerRequestError = getString(R.string.input_field_empty_error);
                                break;

                            case "406:3:bad email":
                                errorPosition = "email";
                                registerRequestError = getString(R.string.login_wrong_input);
                                break;

                            case "406:5:bad password":
                                errorPosition = "password";
                                registerRequestError = getString(R.string.login_wrong_input);
                                break;

                            default:
                                registerRequestError = "Something went wrong, please try again later";
                                Log.wtf(LOG_TAG, "RegisterRequest Error: Unknown Error: " + error);
                        }

                        sb.append(errorPosition);
                        sb.append(": ");
                        sb.append(registerRequestError);
                        sb.append((i == errors.length()-1 ? "" : ", "));


                    }catch (JSONException e){
                        Log.e(LOG_TAG, "JSON Exception", e);
                    }

                }

                Log.d(LOG_TAG, "Register unsuccessful, Errors: " + sb.toString());
                mRegisterChildFragmentList[mRegisterChildFragmentList.length-1].setErrorMessage(getString(R.string.default_unknown_error));
                mRegisterProgressBar.setVisibility(View.INVISIBLE);
                mRegister = false;
            }
        });

    }

    private void sendRegisterRequest(String email, String password, String firstName, String lastName, final RegisterRequestCallback callback){
        RegisterRequest registerRequest = new RegisterRequest(email, password, firstName, lastName, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                try {
                    JSONObject answer = new JSONObject(response);
                    if(answer.getBoolean("success")){
                        callback.onSuccess(answer);
                    }else{
                        callback.onLoginError(answer.getJSONArray("error"));
                    }
                }catch (JSONException e){
                    Log.e(LOG_TAG, "JSON Exception", e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(LOG_TAG,  "RegisterRequest Volley Error", error);
            }
        });

        registerRequest.setTag(REQUEST_TAG);
        ManagerSingleton.getInstance(this.getContext()).addToRequestQueue(registerRequest);
        Log.d(LOG_TAG,"Start RegisterRequest");
    }

    private interface RegisterRequestCallback{
        void onSuccess(JSONObject jsonObject);
        void onLoginError(JSONArray errors);
    }

    // Determines the last fragment to be displayed
    private void backChildFragment(){
        if(mActiveFragment > mRegisterChildFragmentList.length-1){
            mActiveFragment = mRegisterChildFragmentList.length-1;
        }else if(mActiveFragment < 0){
            mActiveFragment = 0;
        }

        if(mActiveFragment != 0){
            mActiveFragment--;
            replaceFragment(mRegisterChildFragmentList[mActiveFragment], false);
        }
    }

    @Override
    public void onNextEmailButtonPressed(String input) {
        mEmail = input;
        nextChildFragment();
    }

    @Override
    public void onNextNameButtonPressed(String firstName, String lastName) {
        mFirstName = firstName;
        mLastName = lastName;
        nextChildFragment();
    }

    @Override
    public void onNextPasswordButtonPressed(String input) {
        mPassword = input;
        nextChildFragment();
    }

    @Override
    public void onBackButtonPressed() {
        backChildFragment();
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

    public String getEmail() {
        return mEmail;
    }

    public String getPassword() {
        return mPassword;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }

}
