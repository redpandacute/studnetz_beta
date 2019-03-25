package com.unnamed.studnetz.main.fragments;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unnamed.studnetz.R;
import com.unnamed.studnetz.view.SubjectView;

import java.util.List;

public class ProfileSubjectsRecyclerViewAdapter
        extends RecyclerView.Adapter<ProfileSubjectsRecyclerViewAdapter.ViewHolder> {

    private String[] mSubject;
    private int[] mBackgroundColor;

    public ProfileSubjectsRecyclerViewAdapter(String[] mData, int[] backgroundColor) {
        this.mSubject = mData;
        this.mBackgroundColor = backgroundColor;
        assert(mSubject.length == mBackgroundColor.length);
    }

    @Override
    public int getItemCount() {
        return mSubject == null ? 0 : mSubject.length;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        SubjectView subject;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            subject = itemView.findViewById(R.id.subjectView_subject_item);
        }
    }

    @NonNull
    @Override
    public ProfileSubjectsRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_subject, parent  ,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.subject.setSubject(mSubject[position]);
        holder.subject.setBackgroundColor(mBackgroundColor[position]);

    }
}
