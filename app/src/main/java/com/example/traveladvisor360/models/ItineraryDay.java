package com.example.traveladvisor360.models;


// 9. ItineraryDay.java

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import com.example.traveladvisor360.models.ItineraryActivity;
public class ItineraryDay {
    private String id;
    private int dayNumber;
    private Date date;
    private String title;
    private List<ItineraryActivity> activities;
    private String notes;

    // Default constructor
    public ItineraryDay() {
    }

    // Constructor with title
    public ItineraryDay(String title) {
        this.title = title;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public int getDayNumber() { return dayNumber; }
    public void setDayNumber(int dayNumber) { this.dayNumber = dayNumber; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public String getTitle() { return title; }

    public List<ItineraryActivity> getActivities() {
        return activities;
    }

    public void setActivities(List<ItineraryActivity> activities) {
        this.activities = activities;
    }


    // Helper methods
    public String getFormattedDate() {
        if (date == null) return "Day " + dayNumber;

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMM d", Locale.getDefault());
        return sdf.format(date);
    }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}