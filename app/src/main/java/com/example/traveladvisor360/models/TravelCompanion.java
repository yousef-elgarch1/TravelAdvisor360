package com.example.traveladvisor360.models;

public class TravelCompanion {
    private String name;
    private String email;
    private String preferences;

    public TravelCompanion(String name, String email, String preferences) {
        this.name = name;
        this.email = email;
        this.preferences = preferences;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPreferences() {
        return preferences;
    }

    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }
}