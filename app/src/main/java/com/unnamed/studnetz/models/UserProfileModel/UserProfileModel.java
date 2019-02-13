package com.unnamed.studnetz.models.UserProfileModel;

import android.content.Context;

import com.unnamed.studnetz.util.CacheManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class UserProfileModel {

    private String mFirstname, mLastname, mDescription, mSchool, mProfilePicturePath;
    private JSONObject mJSON;
    private int mGrade;
    private static final String DUMMY_PROFILEPICTURE_PATH = "PATH";

    public UserProfileModel(JSONObject mJSON, Context mContext) throws JSONException, IOException {

        this.mFirstname = mJSON.getString("mFirstname");
        this.mLastname = mJSON.getString("mLastname");
        this.mDescription = mJSON.getString("mDescription");
        this.mSchool = mJSON.getString("mSchool");

        this.mGrade = mJSON.getInt("mGrade");

        if(!mJSON.has("mProfilePicturePath")) {
            if(mJSON.getString("mProfilePictureBLOB").equals("0")) {
                this.mProfilePicturePath = DUMMY_PROFILEPICTURE_PATH;
            } else {
                this.mProfilePicturePath = new CacheManager(mContext).createCachePicture(mJSON.getString("mProfilePictureBLOB"));
            }

            mJSON.remove("mProfilePictureBLOB");
            mJSON.put("mProfilePicturePath", this.mProfilePicturePath);

        } else {
            this.mProfilePicturePath = mJSON.getString("mProfilePicturePath");
        }

        this.mJSON = mJSON;
    }

    //ACCESSOR FUNCTIONS

    public String getmFirstname() {
        return mFirstname;
    }

    public String getmLastname() {
        return mLastname;
    }

    public String getmDescription() {
        return mDescription;
    }

    public String getmSchool() {
        return mSchool;
    }

    public String getmProfilePicturePath() {
        return mProfilePicturePath;
    }

    public int getmGrade() {
        return mGrade;
    }

    public JSONObject getmJSON() {
        return mJSON;
    }

    //MUTATOR FUNCTIONS

    public void setmFirstname(String mFirstname) throws JSONException {
        this.mFirstname = mFirstname;
        this.mJSON.put("mFirstname", mFirstname);
    }

    public void setmLastname(String mLastname) throws JSONException {
        this.mLastname = mLastname;
        this.mJSON.put("mLastname", mLastname);
    }

    public void setmDescription(String mDescription) throws JSONException {
        this.mDescription = mDescription;
        this.mJSON.put("mDescription", mDescription);
    }

    public void setmSchool(String mSchool) throws JSONException {
        this.mSchool = mSchool;
        this.mJSON.put("mSchool", mSchool);
    }

    public void setmProfilePicturePath(String mProfilePicturePath) throws JSONException {
        this.mProfilePicturePath = mProfilePicturePath;
        this.mJSON.put("mProfilePicturePath", mProfilePicturePath);
    }

    public void setmGrade(int mGrade) throws JSONException {
        this.mGrade = mGrade;
        this.mJSON.put("mGrade", mGrade);
    }

}
