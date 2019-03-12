package com.unnamed.studnetz.main.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unnamed.studnetz.R;

public class ProfileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_clientprofile_temp1, null);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_subjects_client_profile);
        ProfileSubjectsRecyclerViewAdapter adapter
                = new ProfileSubjectsRecyclerViewAdapter(new String[]{"Physic", "Math" , "French", "German", "English", "French", "German", "English"}, new int[] {getResources().getColor(R.color.colorErrorRed), getResources().getColor(R.color.colorPrimaryDark), getResources().getColor(R.color.black_overlay), getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.colorAccent) ,getResources().getColor(R.color.light_grey) , getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.colorAccent) ,getResources().getColor(R.color.light_grey) });

        recyclerView.setAdapter(adapter);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
