package com.google.prochat.Models;

public class ChatMessage {

    private String messageId, message, userid;
    private long timeStamp;

    public ChatMessage()
    {

    }

    public ChatMessage(String messageId, String message, String userid, long timeStamp) {
        this.messageId = messageId;
        this.message = message;
        this.userid = userid;
        this.timeStamp = timeStamp;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}

