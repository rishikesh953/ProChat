package com.google.prochat.Models;

public class User {

    private  String mUid, mUsername, mPhoneNumber, mProfileImage, mDOB;


    public User()
    {

    }


    public User(String mUid, String mUsername, String mPhoneNumber, String mProfileImage,
                String mDOB)
    {
        this.mUid = mUid;
        this.mUsername = mUsername;
        this.mPhoneNumber = mPhoneNumber;
        this.mProfileImage = mProfileImage;
        this.mDOB = mDOB;
    }


    public void setUid(String mUid)
    {
        this.mUid = mUid;
    }

    public void setUsername(String mUsername)
    {
        this.mUsername = mUsername;
    }

    public void setPhoneNumber(String mPhoneNumber)
    {
        this.mPhoneNumber = mPhoneNumber;
    }

    public void setProfileImage(String mProfileImage)
    {
        this.mProfileImage = mProfileImage;
    }

    public String getUid() { return mUid; }
    public String getUsername() { return mUsername; }
    public String getPhoneNumber() { return mPhoneNumber; }
    public String getProfileImage() { return mProfileImage; }
    public String getDOB()  { return mDOB; }


}
