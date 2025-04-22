package com.example.traveladvisor360.models;

import java.util.Date;

public class ItineraryActivity {
    private String id;
    private String title;
    private String type; // "FLIGHT", "HOTEL", "ATTRACTION", "RESTAURANT", "OTHER"
    private String location;
    private Date startTime;
    private Date endTime;
    private String notes;
    private String bookingReference;
    private double cost;
    private String currency;

    public ItineraryActivity() {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getBookingReference() {
        return bookingReference;
    }

    public void setBookingReference(String bookingReference) {
        this.bookingReference = bookingReference;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    // Helper methods
    public String getFormattedTime() {
        if (startTime == null) return "";

        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("h:mm a", java.util.Locale.getDefault());
        if (endTime == null) {
            return sdf.format(startTime);
        } else {
            return sdf.format(startTime) + " - " + sdf.format(endTime);
        }
    }

    public String getFormattedCost() {
        if (cost <= 0 || currency == null) return "";

        return String.format("%s %.2f", currency, cost);
    }

    public int getTypeIcon() {
        // Return appropriate drawable resource ID based on type
        switch (type != null ? type : "") {
            case "FLIGHT":
                return android.R.drawable.ic_menu_send; // Placeholder
            case "HOTEL":
                return android.R.drawable.ic_menu_myplaces; // Placeholder
            case "ATTRACTION":
                return android.R.drawable.ic_menu_mapmode; // Placeholder
            case "RESTAURANT":
                return android.R.drawable.ic_menu_slideshow; // Placeholder
            default:
                return android.R.drawable.ic_menu_agenda; // Placeholder
        }
    }
}