package com.moutamid.hbdresidents.models;

public class ComplaintModel {
    String id, userID, title, description, type, image;
    long timestamp;
    boolean urgent;

    public ComplaintModel() {}

    public ComplaintModel(String id, String userID, String title, String description, String type, String image, long timestamp, boolean urgent) {
        this.id = id;
        this.userID = userID;
        this.title = title;
        this.description = description;
        this.type = type;
        this.image = image;
        this.timestamp = timestamp;
        this.urgent = urgent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isUrgent() {
        return urgent;
    }

    public void setUrgent(boolean urgent) {
        this.urgent = urgent;
    }
}
