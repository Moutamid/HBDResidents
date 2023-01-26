package com.moutamid.hbdresidents.models;

public class UserModel {
    String uid, name, residentialArea, dob, email, password, image;

    public UserModel() {
    }

    public UserModel(String uid, String name, String residentialArea, String dob, String email, String password, String image) {
        this.uid = uid;
        this.name = name;
        this.residentialArea = residentialArea;
        this.dob = dob;
        this.email = email;
        this.password = password;
        this.image = image;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResidentialArea() {
        return residentialArea;
    }

    public void setResidentialArea(String residentialArea) {
        this.residentialArea = residentialArea;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
