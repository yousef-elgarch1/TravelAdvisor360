package com.example.traveladvisor360.models;

public class Chemin {

    private String id;
    private String title;
    private String location;
    private String dateRange;
    private int imageResId;
    private String buttonTextKey;

    public Chemin(String id, String title, String location, String dateRange, int imageResId, String buttonTextKey) {
        this.id = id;
        this.title = title;
        this.location = location;
        this.dateRange = dateRange;
        this.imageResId = imageResId;
        this.buttonTextKey = buttonTextKey;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public String getDateRange() {
        return dateRange;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getButtonTextKey() {
        return buttonTextKey;
    }
}
