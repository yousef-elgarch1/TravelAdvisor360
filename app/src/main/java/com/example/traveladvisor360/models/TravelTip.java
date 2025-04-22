package com.example.traveladvisor360.models;

public class TravelTip {
    private String id;
    private String title;
    private String description;
    private String category;
    private String imageUrl;
    private String content;
    private boolean isBookmarked;

    // Default constructor
    public TravelTip() {
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public boolean isBookmarked() { return isBookmarked; }
    public void setBookmarked(boolean bookmarked) { isBookmarked = bookmarked; }
}
