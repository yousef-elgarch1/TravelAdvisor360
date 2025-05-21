package com.example.traveladvisor360.models;

import java.util.Date;

public class Comment {
    private String id;
    private String userId;
    private String userName;
    private String userPhotoUrl;
    private String content;
    private Date createdAt;
    private int likeCount;
    private boolean isLiked;

    public Comment() {
        this.createdAt = new Date();
    }

    // Constructor with content and user info
    public Comment(String userId, String userName, String userPhotoUrl, String content) {
        this.userId = userId;
        this.userName = userName;
        this.userPhotoUrl = userPhotoUrl;
        this.content = content;
        this.createdAt = new Date();
        this.likeCount = 0;
        this.isLiked = false;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhotoUrl() {
        return userPhotoUrl;
    }

    public void setUserPhotoUrl(String userPhotoUrl) {
        this.userPhotoUrl = userPhotoUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    // Toggle like state
    public void toggleLike() {
        this.isLiked = !this.isLiked;
        this.likeCount += this.isLiked ? 1 : -1;
    }
}