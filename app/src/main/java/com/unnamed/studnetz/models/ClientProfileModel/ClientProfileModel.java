package com.unnamed.studnetz.models.ClientProfileModel;

import android.content.Context;

import com.unnamed.studnetz.util.CacheManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class ClientProfileModel {

    private String mFirstname, mLastname, mDescription, mSchool, mProfilePicturePath, mEmail, mPasswordHash;
    private JSONObject mJSON;
    private int mGrade;
    private static final String DUMMY_PROFILEPICTURE_PATH = "PATH";

    public ClientProfileModel(JSONObject JSON, Context context) throws JSONException, IOException {

        this.mFirstname = JSON.getString("firstname");
        this.mLastname = JSON.getString("lastname");
        this.mDescription = JSON.getString("Description");
        this.mSchool = JSON.getString("School");
        this.mEmail = JSON.getString("Email");
        this.mPasswordHash = JSON.getString("PasswordHash");

        this.mGrade = JSON.getInt("Grade");

        if(!JSON.has("ProfilePicturePath")) {
            if(JSON.getString("ProfilePictureBLOB").equals("0")) {
                this.mProfilePicturePath = DUMMY_PROFILEPICTURE_PATH;
            } else {
                this.mProfilePicturePath = new CacheManager(context).createCachePicture(JSON.getString("ProfilePictureBLOB"));
            }

            JSON.remove("ProfilePictureBLOB");
            JSON.put("ProfilePicturePath", this.mProfilePicturePath);

        } else {
            this.mProfilePicturePath = JSON.getString("ProfilePicturePath");
        }

        this.mJSON = JSON;
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

    public String getmEmail() {
        return mEmail;
    }

    public String getmPasswordHash() {
        return mPasswordHash;
    }

    public JSONObject getmJSON() {
        return mJSON;
    }

    public int getmGrade() {
        return mGrade;
    }


    //MUTATOR FUNCTIONS

    public void setmFirstname(String mFirstname) throws JSONException {
        this.mFirstname = mFirstname;
        this.mJSON.put("Firstname", mFirstname);
    }

    public void setmLastname(String mLastname) throws JSONException {
        this.mLastname = mLastname;
        this.mJSON.put("Lastname", mLastname);
    }

    public void setmDescription(String mDescription) throws JSONException {
        this.mDescription = mDescription;
        this.mJSON.put("Description", mDescription);
    }

    public void setmSchool(String mSchool) throws JSONException {
        this.mSchool = mSchool;
        this.mJSON.put("School", mSchool);
    }

    public void setmProfilePicturePath(String mProfilePicturePath) throws JSONException {
        this.mProfilePicturePath = mProfilePicturePath;
        this.mJSON.put("ProfilePicturePath", mProfilePicturePath);
    }

    public void setmEmail(String mEmail) throws JSONException {
        this.mEmail = mEmail;
        this.mJSON.put("Email", mEmail);
    }

    public void setmGrade(int mGrade) throws JSONException {
        this.mGrade = mGrade;
        this.mJSON.put("Grade", mGrade);
    }

}
