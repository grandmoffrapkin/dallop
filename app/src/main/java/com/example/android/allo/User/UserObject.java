package com.example.android.allo.User;

import android.net.Uri;

import java.io.Serializable;

public class UserObject implements Serializable {

    private String uid, name, phone, notificationKey;
    private Uri imageUri;

    private Boolean selected = false;

    public UserObject(String uid){
        this.uid = uid;
    }

    public UserObject(String uid, String name, String phone, Uri imageUri) {
        this.uid = uid;
        this.name = name;
        this.phone = phone;
        this.imageUri = imageUri;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public String getNotificationKey() {
        return notificationKey;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public void setNotificationKey(String notificationKey) {
        this.notificationKey = notificationKey;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }
}
