package com.unnamed.studnetz.models.UserProfileModel;

import org.json.JSONException;
import org.json.JSONObject;

public class UserProfileModel {

    private String mFirstname, mLastname, mDescription, mSchool, mProfilePicturePath;
    private JSONObject mJSON;
    private int mGrade;

    public UserProfileModel(JSONObject mJSON) {

        this.mJSON = mJSON;

        try {

            this.mFirstname = mJSON.getString("mFirstname");
            this.mLastname = mJSON.getString("mLastname");
            this.mDescription = mJSON.getString("mDescription");
            this.mSchool = mJSON.getString("mSchool");

            this.mGrade = mJSON.getInt("mGrade");

        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("FAILED TO CREATE INSTANCE OF USERPROFILEMODEL: JSON(" + mJSON.toString() + ")");
        }
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

    public void setmFirstname(String mFirstname) {
        this.mFirstname = mFirstname;
    }

    public void setmLastname(String mLastname) {
        this.mLastname = mLastname;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public void setmSchool(String mSchool) {
        this.mSchool = mSchool;
    }

    public void setmProfilePicturePath(String mProfilePicturePath) {
        this.mProfilePicturePath = mProfilePicturePath;
    }

    public void setmGrade(int mGrade) {
        this.mGrade = mGrade;
    }
}
