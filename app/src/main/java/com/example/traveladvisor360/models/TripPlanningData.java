package com.example.traveladvisor360.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TripPlanningData {
    private static TripPlanningData instance;

    // Trip details
    private String tripType;
    private String destination;
    private double budget;
    private String currency;
    private List<TravelCompanion> companions = new ArrayList<>();
    private List<String> selectedActivities = new ArrayList<>();
    private String selectedFlight;
    private Map<String, Object> flightDetails = new HashMap<>();
    private String selectedHotel;
    private Map<String, Object> hotelDetails = new HashMap<>();

    // Private constructor for singleton
    private TripPlanningData() {}

    // Singleton accessor
    public static synchronized TripPlanningData getInstance() {
        if (instance == null) {
            instance = new TripPlanningData();
        }
        return instance;
    }

    // Method to reset data for a new trip
    public void resetData() {
        tripType = null;
        destination = null;
        budget = 0;
        currency = null;
        companions.clear();
        selectedActivities.clear();
        selectedFlight = null;
        flightDetails.clear();
        selectedHotel = null;
        hotelDetails.clear();
    }

    // Getters and setters
    public String getTripType() {
        return tripType;
    }

    public void setTripType(String tripType) {
        this.tripType = tripType;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public List<TravelCompanion> getCompanions() {
        return companions;
    }

    public void addCompanion(TravelCompanion companion) {
        companions.add(companion);
    }

    public boolean hasCompanions() {
        return !companions.isEmpty();
    }

    public List<String> getSelectedActivities() {
        return selectedActivities;
    }

    public void addActivity(String activity) {
        selectedActivities.add(activity);
    }

    public void removeActivity(String activity) {
        selectedActivities.remove(activity);
    }

    public String getSelectedFlight() {
        return selectedFlight;
    }

    public void setSelectedFlight(String selectedFlight) {
        this.selectedFlight = selectedFlight;
    }

    public Map<String, Object> getFlightDetails() {
        return flightDetails;
    }

    public void setFlightDetails(Map<String, Object> flightDetails) {
        this.flightDetails = flightDetails;
    }

    public String getSelectedHotel() {
        return selectedHotel;
    }

    public void setSelectedHotel(String selectedHotel) {
        this.selectedHotel = selectedHotel;
    }

    public Map<String, Object> getHotelDetails() {
        return hotelDetails;
    }

    public void setHotelDetails(Map<String, Object> hotelDetails) {
        this.hotelDetails = hotelDetails;
    }
}