package com.example.android.allo.Chat;

import android.net.Uri;

import com.example.android.allo.User.UserObject;

import java.io.Serializable;
import java.util.ArrayList;

public class ChatObject implements Serializable {

    private String chatId;

    private String contactName, lastMessage;

    private Uri profileImageUri;

    private ArrayList<UserObject> userObjectArrayList = new ArrayList<>();

    public ChatObject(String chatId, String contactName, String lastMessage, Uri profileImageUri) {
        this.chatId = chatId;
        this.contactName = contactName;
        this.lastMessage = lastMessage;
        this.profileImageUri = profileImageUri;
    }

    public String getChatId() {
        return chatId;
    }

    public String getContactName() {
        return contactName;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public Uri getProfileImageUri() {
        return profileImageUri;
    }

    public ArrayList<UserObject> getUserObjectArrayList() {
        return userObjectArrayList;
    }

    public void addUserToArrayList(UserObject mUser) {
        userObjectArrayList.add(mUser);
    }
}
