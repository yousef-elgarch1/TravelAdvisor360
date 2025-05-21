package com.example.traveladvisor360.models;

import java.io.Serializable;

public class User implements Serializable {
    private String id;
    private String email;
    private String name;
    private String username;
    private String bio;
    private String profilePicUrl;
    private String password;
    private boolean isEmailVerified;
    private long createdAt;
    private UserPreferences preferences;

    // Stats for profile display
    private int tripsCount;
    private int countriesCount;
    private int photosCount;

    // Required empty constructor
    public User() {
    }

    public User(String id, String email, String name, String profilePicUrl, boolean isEmailVerified) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.profilePicUrl = profilePicUrl;
        this.isEmailVerified = isEmailVerified;
        this.createdAt = System.currentTimeMillis();
        this.preferences = new UserPreferences();

        // Default values for stats
        this.tripsCount = 0;
        this.countriesCount = 0;
        this.photosCount = 0;

        // Generate a default username from email
        if (email != null && email.contains("@")) {
            this.username = email.substring(0, email.indexOf('@'));
        } else {
            this.username = "traveler" + createdAt;
        }

        // Default bio
        this.bio = "Travel enthusiast";
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getProfilePicUrl() { return profilePicUrl; }
    public void setProfilePicUrl(String profilePicUrl) { this.profilePicUrl = profilePicUrl; }

    public boolean isEmailVerified() { return isEmailVerified; }
    public void setEmailVerified(boolean emailVerified) { isEmailVerified = emailVerified; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    public UserPreferences getPreferences() { return preferences; }
    public void setPreferences(UserPreferences preferences) { this.preferences = preferences; }

    // New getters and setters for profile display
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public int getTripsCount() { return tripsCount; }
    public void setTripsCount(int tripsCount) { this.tripsCount = tripsCount; }

    public int getCountriesCount() { return countriesCount; }
    public void setCountriesCount(int countriesCount) { this.countriesCount = countriesCount; }

    public int getPhotosCount() { return photosCount; }
    public void setPhotosCount(int photosCount) { this.photosCount = photosCount; }

    // UserPreferences inner class
    public static class UserPreferences implements Serializable {
        private boolean receiveNotifications = true;
        private String[] travelInterests = {"Adventure", "Cultural", "Food", "Nature"};

        public UserPreferences() {
            // Default constructor
        }

        public boolean isReceiveNotifications() { return receiveNotifications; }
        public void setReceiveNotifications(boolean receiveNotifications) {
            this.receiveNotifications = receiveNotifications;
        }

        public String[] getTravelInterests() { return travelInterests; }
        public void setTravelInterests(String[] travelInterests) {
            this.travelInterests = travelInterests;
        }
    }
}