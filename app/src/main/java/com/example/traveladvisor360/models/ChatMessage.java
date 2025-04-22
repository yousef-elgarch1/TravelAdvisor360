package com.example.traveladvisor360.models;

import java.util.Date;

public class ChatMessage {
    private String id;
    private String sender; // "user" or "bot"
    private String message;
    private Date timestamp;
    private String type; // "text", "image", "recommendation"
    private Object attachment; // For non-text messages

    // Default constructor
    public ChatMessage() {
        this.timestamp = new Date();
        this.type = "text";
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Date getTimestamp() { return timestamp; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Object getAttachment() { return attachment; }
    public void setAttachment(Object attachment) { this.attachment = attachment; }
}
