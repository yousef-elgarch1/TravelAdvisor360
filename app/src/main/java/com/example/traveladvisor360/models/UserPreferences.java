package com.example.traveladvisor360.models;

import java.util.ArrayList;
import java.util.List;

public class UserPreferences {
    private List<String> favoriteDestinations;
    private List<String> travelInterests;
    private String preferredCurrency;
    private String preferredLanguage;
    private boolean receiveNotifications;
    private boolean receivePromotionalEmails;

    public UserPreferences() {
        this.favoriteDestinations = new ArrayList<>();
        this.travelInterests = new ArrayList<>();
        this.preferredCurrency = "USD";
        this.preferredLanguage = "EN";
        this.receiveNotifications = true;
        this.receivePromotionalEmails = false;
    }

    // Getters and setters
    public List<String> getFavoriteDestinations() { return favoriteDestinations; }
    public void setFavoriteDestinations(List<String> favoriteDestinations) { this.favoriteDestinations = favoriteDestinations; }

    public List<String> getTravelInterests() { return travelInterests; }
    public void setTravelInterests(List<String> travelInterests) { this.travelInterests = travelInterests; }

    public String getPreferredCurrency() { return preferredCurrency; }
    public void setPreferredCurrency(String preferredCurrency) { this.preferredCurrency = preferredCurrency; }

    public String getPreferredLanguage() { return preferredLanguage; }
    public void setPreferredLanguage(String preferredLanguage) { this.preferredLanguage = preferredLanguage; }

    public boolean isReceiveNotifications() { return receiveNotifications; }
    public void setReceiveNotifications(boolean receiveNotifications) { this.receiveNotifications = receiveNotifications; }

    public boolean isReceivePromotionalEmails() { return receivePromotionalEmails; }
    public void setReceivePromotionalEmails(boolean receivePromotionalEmails) { this.receivePromotionalEmails = receivePromotionalEmails; }
}
