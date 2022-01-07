package com.google.prochat.Models;

import java.util.ArrayList;

public class UserStatus {

    private String username, profileImage;
    private  long lastUpdated;
    private ArrayList<StatusClass> statusClasses;

    public UserStatus() {
    }

    public UserStatus(String username, String profileImage, long lastUpdated, ArrayList<StatusClass> statusClasses) {
        this.username = username;
        this.profileImage = profileImage;
        this.lastUpdated = lastUpdated;
        this.statusClasses = statusClasses;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public ArrayList<StatusClass> getStatusClasses() {
        return statusClasses;
    }

    public void setStatusClasses(ArrayList<StatusClass> statusClasses) {
        this.statusClasses = statusClasses;
    }
}
