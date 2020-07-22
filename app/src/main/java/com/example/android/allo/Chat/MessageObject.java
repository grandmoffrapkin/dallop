package com.example.android.allo.Chat;

public class MessageObject {

    String messageId, senderId, message;

    public MessageObject(String messageId, String senderId, String message) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.message = message;
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

    public void getCreatedAt() {
    }
}
