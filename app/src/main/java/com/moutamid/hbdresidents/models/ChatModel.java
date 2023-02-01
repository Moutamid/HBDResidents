package com.moutamid.hbdresidents.models;

public class ChatModel {
    String message;
    String senderID;
    long timestamps;

    public ChatModel() {
    }

    public ChatModel(String message, String senderID, long timestamps) {
        this.message = message;
        this.senderID = senderID;
        this.timestamps = timestamps;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public long getTimestamps() {
        return timestamps;
    }

    public void setTimestamps(long timestamps) {
        this.timestamps = timestamps;
    }
}
