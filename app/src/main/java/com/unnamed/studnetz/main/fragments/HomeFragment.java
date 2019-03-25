package com.unnamed.studnetz.main.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.unnamed.studnetz.R;
import com.unnamed.studnetz.main.MainActivity;

public class HomeFragment extends Fragment {



    public interface OnFragmentInteractionListener{
        void onHomeFragmentInteraction(View v);
    }

    private OnFragmentInteractionListener mListener;
    ImageButton mSearchButton;

    Button logOut;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         final View mView = inflater.inflate(R.layout.fragment_home, null);

        mSearchButton = mView.findViewById(R.id.fragment_home_searchbutton);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onHomeFragmentInteraction(v);
            }
        });

        logOut = mView.findViewById(R.id.temp_logOut_button);
        ((View) logOut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             mListener.onHomeFragmentInteraction(v);
            }
        });

        return mView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnFragmentInteractionListener){
            mListener = (OnFragmentInteractionListener) context;
        }else{
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
