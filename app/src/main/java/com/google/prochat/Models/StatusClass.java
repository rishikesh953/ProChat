package com.google.prochat.Models;

public class StatusClass {

    private String statusImage;
    private long timeStamp;

    public StatusClass()
    {

    }

    public StatusClass(String statusImage, long timeStamp) {
        this.statusImage = statusImage;
        this.timeStamp = timeStamp;
    }

    public String getStatusImage() {
        return statusImage;
    }

    public void setStatusImage(String statusImage) {
        this.statusImage = statusImage;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
