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
    private String destinationCityName; // <-- Add this field
    private double budget;
    private String currency;
    private String startDate;
    private String returnDate;
    private String bearerToken;
    private List<TravelCompanion> companions = new ArrayList<>();
    private List<String> selectedActivities = new ArrayList<>();
    private String departure;
    private String selectedFlight;
    private Map<String, Object> flightDetails = new HashMap<>();
    private String selectedHotel;
    private Map<String, Object> hotelDetails = new HashMap<>();

    private TripPlanningData() {}

    public static TripPlanningData getInstance() {
        if (instance == null) {
            instance = new TripPlanningData();
        }
        return instance;
    }

    // Trip type
    public String getTripType() { return tripType; }
    public void setTripType(String tripType) { this.tripType = tripType; }

    // Destination IATA code
    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    // Destination city name
    public String getDestinationCityName() { return destinationCityName; }
    public void setDestinationCityName(String name) { this.destinationCityName = name; }

    // Budget
    public double getBudget() { return budget; }
    public void setBudget(double budget) { this.budget = budget; }

    // Currency
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    // Dates
    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public String getReturnDate() { return returnDate; }
    public void setReturnDate(String returnDate) { this.returnDate = returnDate; }

    // Bearer token
    public String getBearerToken() { return bearerToken; }
    public void setBearerToken(String bearerToken) { this.bearerToken = bearerToken; }

    // Companions
    public List<TravelCompanion> getCompanions() { return companions; }
    public void setCompanions(List<TravelCompanion> companions) { this.companions = companions; }

    // Activities
    public List<String> getSelectedActivities() { return selectedActivities; }
    public void setSelectedActivities(List<String> selectedActivities) { this.selectedActivities = selectedActivities; }

    // Departure IATA code
    public String getDeparture() { return departure; }
    public void setDeparture(String departure) { this.departure = departure; }

    // Flight selection
    public String getSelectedFlight() { return selectedFlight; }
    public void setSelectedFlight(String selectedFlight) { this.selectedFlight = selectedFlight; }
    public Map<String, Object> getFlightDetails() { return flightDetails; }
    public void setFlightDetails(Map<String, Object> flightDetails) { this.flightDetails = flightDetails; }

    // Hotel selection
    public String getSelectedHotel() { return selectedHotel; }
    public void setSelectedHotel(String selectedHotel) { this.selectedHotel = selectedHotel; }
    public Map<String, Object> getHotelDetails() { return hotelDetails; }
    public void setHotelDetails(Map<String, Object> hotelDetails) { this.hotelDetails = hotelDetails; }

    // Reset all data (optional utility)
    public void reset() {
        tripType = null;
        destination = null;
        destinationCityName = null;
        budget = 0;
        currency = null;
        startDate = null;
        returnDate = null;
        bearerToken = null;
        companions.clear();
        selectedActivities.clear();
        departure = null;
        selectedFlight = null;
        flightDetails.clear();
        selectedHotel = null;
        hotelDetails.clear();
    }
}