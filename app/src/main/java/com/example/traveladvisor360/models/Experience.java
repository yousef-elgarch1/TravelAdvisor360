package com.example.traveladvisor360.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Experience {
    // Basic Experience Info
    private String id;
    private String title;
    private String location;
    private String description;
    private String imageUrl;
    private String category;
    private float rating;
    private int reviewCount;
    private double price;
    private float duration;
    private String hostInfo;
    private String cancellationPolicy;
    private List<String> requirements;

    // Social features
    private String userId;             // User who posted the experience
    private String userName;           // Name of the user who posted
    private String userPhotoUrl;       // Profile photo
    private Date createdAt;            // When the experience was posted
    private int likeCount;             // Number of likes
    private boolean isLiked;           // To track if current user liked it
    private List<Comment> comments;    // Comments on the experience
    private int viewCount;             // Number of views
    private boolean isFeatured;        // If this is a featured experience

    // Default constructor
    public Experience() {
        this.comments = new ArrayList<>();
        this.createdAt = new Date();
        this.requirements = new ArrayList<>();
    }

    // Basic Info Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public String getHostInfo() {
        return hostInfo;
    }

    public void setHostInfo(String hostInfo) {
        this.hostInfo = hostInfo;
    }

    public String getCancellationPolicy() {
        return cancellationPolicy;
    }

    public void setCancellationPolicy(String cancellationPolicy) {
        this.cancellationPolicy = cancellationPolicy;
    }

    public List<String> getRequirements() {
        return requirements;
    }

    public void setRequirements(List<String> requirements) {
        this.requirements = requirements;
    }

    // Social Feature Getters and Setters
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

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public boolean isFeatured() {
        return isFeatured;
    }

    public void setFeatured(boolean featured) {
        isFeatured = featured;
    }

    // Helper methods for social features
    public void addComment(Comment comment) {
        if (this.comments == null) {
            this.comments = new ArrayList<>();
        }
        this.comments.add(comment);
    }

    public void toggleLike() {
        this.isLiked = !this.isLiked;
        this.likeCount += this.isLiked ? 1 : -1;
    }

    // Helper method to get duration as string
    public String getDurationString() {
        if (duration < 1) {
            return Math.round(duration * 60) + " minutes";
        } else if (duration == 1) {
            return "1 hour";
        } else if (duration % 1 == 0) {
            return (int) duration + " hours";
        } else {
            int hours = (int) duration;
            int minutes = Math.round((duration - hours) * 60);
            return hours + " hours " + minutes + " minutes";
        }
    }
}