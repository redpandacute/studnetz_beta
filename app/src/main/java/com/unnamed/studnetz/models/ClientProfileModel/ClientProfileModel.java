package com.unnamed.studnetz.models.ClientProfileModel;

import org.json.JSONException;
import org.json.JSONObject;

public class ClientProfileModel {

    private String mFirstname, mLastname, mDescription, mSchool, mProfilePicturePath, mEmail, mPasswordHash;
    private JSONObject mJSON;
    private int mGrade;

    public ClientProfileModel(JSONObject mJSON) throws JSONException {

        this.mJSON = mJSON;


        this.mFirstname = mJSON.getString("mFirstname");
        this.mLastname = mJSON.getString("mLastname");
        this.mDescription = mJSON.getString("mDescription");
        this.mSchool = mJSON.getString("mSchool");
        this.mEmail = mJSON.getString("mEmail");
        this.mPasswordHash = mJSON.getString("mPasswordHash");

        this.mGrade = mJSON.getInt("mGrade");

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

    public void setmEmail(String mEmail) throws JSONException {
        this.mEmail = mEmail;
        this.mJSON.put("mEmail", mEmail);
    }

    public void setmGrade(int mGrade) throws JSONException {
        this.mGrade = mGrade;
        this.mJSON.put("mGrade", mGrade);
    }
}
