package com.example.traveladvisor360.models;

public class HotelOption {
    private String name;
    private String location;
    private String description;
    private double rating;
    private double pricePerNight;
    private int stars;
    private boolean selected;

    public HotelOption(String name, String location, String description,
                       double rating, double pricePerNight, int stars, boolean selected) {
        this.name = name;
        this.location = location;
        this.description = description;
        this.rating = rating;
        this.pricePerNight = pricePerNight;
        this.stars = stars;
        this.selected = selected;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}