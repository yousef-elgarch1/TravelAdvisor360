package com.example.traveladvisor360.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class SavedItinerary {
    private String id;
    private String tripType;
    private String destination;
    private String destinationCityName;
    private String departure;
    private double budget;
    private String currency;
    private Date startDate;
    private Date returnDate;
    private String selectedFlight;
    private Map<String, Object> flightDetails;
    private String selectedHotel;
    private Map<String, Object> hotelDetails;
    private List<String> selectedActivities;
    private List<TravelCompanion> companions;
    private Date createdAt;

    public SavedItinerary() {
        this.id = java.util.UUID.randomUUID().toString();
        this.createdAt = new Date();
        this.selectedActivities = new ArrayList<>();
        this.companions = new ArrayList<>();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTripType() { return tripType; }
    public void setTripType(String tripType) { this.tripType = tripType; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public String getDestinationCityName() { return destinationCityName; }
    public void setDestinationCityName(String destinationCityName) { this.destinationCityName = destinationCityName; }

    public String getDeparture() { return departure; }
    public void setDeparture(String departure) { this.departure = departure; }

    public double getBudget() { return budget; }
    public void setBudget(double budget) { this.budget = budget; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getReturnDate() { return returnDate; }
    public void setReturnDate(Date returnDate) { this.returnDate = returnDate; }

    public String getSelectedFlight() { return selectedFlight; }
    public void setSelectedFlight(String selectedFlight) { this.selectedFlight = selectedFlight; }

    public Map<String, Object> getFlightDetails() { return flightDetails; }
    public void setFlightDetails(Map<String, Object> flightDetails) { this.flightDetails = flightDetails; }

    public String getSelectedHotel() { return selectedHotel; }
    public void setSelectedHotel(String selectedHotel) { this.selectedHotel = selectedHotel; }

    public Map<String, Object> getHotelDetails() { return hotelDetails; }
    public void setHotelDetails(Map<String, Object> hotelDetails) { this.hotelDetails = hotelDetails; }

    public List<String> getSelectedActivities() { return selectedActivities; }
    public void setSelectedActivities(List<String> selectedActivities) { this.selectedActivities = selectedActivities; }

    public List<TravelCompanion> getCompanions() { return companions; }
    public void setCompanions(List<TravelCompanion> companions) { this.companions = companions; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    // Helper method to check if trip is upcoming
    public boolean isUpcoming() {
        Date now = new Date();
        return startDate.after(now);
    }

    // Helper method to check if trip is current
    public boolean isCurrent() {
        Date now = new Date();
        return !startDate.after(now) && !returnDate.before(now);
    }

    // Helper method to check if trip is past
    public boolean isPast() {
        Date now = new Date();
        return returnDate.before(now);
    }
} 