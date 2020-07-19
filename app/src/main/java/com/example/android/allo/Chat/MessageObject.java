package com.example.android.allo.Chat;

import java.util.ArrayList;

public class MessageObject {

    String messageId, senderId, message;
    ArrayList<String> mediaUrlList;

    public MessageObject(String messageId, String senderId, String message, ArrayList<String> mediaUrlList) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.message = message;
        this.mediaUrlList = mediaUrlList;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getMessageId() {
        return messageId;
    }

    public ArrayList<String> getMediaUrlList() {
        return mediaUrlList;
    }

    public void getCreatedAt() {
    }
}
