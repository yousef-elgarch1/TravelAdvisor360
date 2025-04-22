package com.example.traveladvisor360.utils;

import java.util.List;

public class TripData {

    private String destination;
    private String budget;
    private String tripType;
    private String startDate;
    private String endDate;
    private int travelers;
    private List<String> interests; // Example: ["Beach", "Adventure", "Nature"]

    // Constructor
    public TripData(String destination, String budget, String tripType, String startDate, String endDate, int travelers, List<String> interests) {
        this.destination = destination;
        this.budget = budget;
        this.tripType = tripType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.travelers = travelers;
        this.interests = interests;
    }

    // Getters and Setters
    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getTripType() {
        return tripType;
    }

    public void setTripType(String tripType) {
        this.tripType = tripType;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getTravelers() {
        return travelers;
    }

    public void setTravelers(int travelers) {
        this.travelers = travelers;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }

    // You can also add methods to return a summary of the trip, or other helper methods.
    public String getTripSummary() {
        return "Destination: " + destination + "\n" +
                "Budget: " + budget + "\n" +
                "Trip Type: " + tripType + "\n" +
                "Dates: " + startDate + " to " + endDate + "\n" +
                "Travelers: " + travelers + "\n" +
                "Interests: " + String.join(", ", interests);
    }
}

