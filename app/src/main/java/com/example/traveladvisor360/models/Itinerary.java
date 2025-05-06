package com.example.traveladvisor360.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Itinerary {
    private String id;
    private String title;
    private String destination;
    private Date startDate;
    private Date endDate;
    private String coverImage; // Main field for cover image
    private String notes;
    private List<ItineraryDay> days;
    private String userId;
    private boolean isCollaborative;
    private double budget;
    private Date createdAt;
    private Date updatedAt;

    private int travelerCount;


    private String name;
    private String description;
    private String duration;
    private String price;
    private String imageUrl;

    public Itinerary() {
        days = new ArrayList<>();
    }

    // Getters and setters
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

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    // Cover image methods - both naming patterns to support different usage
    public String getCoverImage() {
        return coverImage;
    }

    public String getCoverImageUrl() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImage = coverImageUrl;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<ItineraryDay> getDays() {
        return days;
    }

    public void setDays(List<ItineraryDay> days) {
        this.days = days;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isCollaborative() {
        return isCollaborative;
    }

    public void setCollaborative(boolean collaborative) {
        isCollaborative = collaborative;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }



    public String getFormattedDateRange() {
        if (startDate == null || endDate == null) return "";

        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMM d, yyyy", java.util.Locale.getDefault());
        return sdf.format(startDate) + " - " + sdf.format(endDate);
    }

    /**
     * Sets the number of travelers for this itinerary
     * @param travelerCount The number of travelers
     */
    public void setTravelerCount(int travelerCount) {
        this.travelerCount = travelerCount;
    }

    /**
     * Gets the number of travelers for this itinerary
     * @return The number of travelers
     */
    public int getTravelerCount() {
        return travelerCount;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDuration() {
        return duration;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice() {
        return price;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}