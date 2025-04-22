package com.example.traveladvisor360.models;

public class User {
    private String id;
    private String email;
    private String name;
    private String profilePicUrl;
    private boolean isEmailVerified;
    private long createdAt;
    private UserPreferences preferences;

    public User() {
        // Required empty constructor
    }

    public User(String id, String email, String name, String profilePicUrl, boolean isEmailVerified) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.profilePicUrl = profilePicUrl;
        this.isEmailVerified = isEmailVerified;
        this.createdAt = System.currentTimeMillis();
        this.preferences = new UserPreferences();
    }


    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getProfilePicUrl() { return profilePicUrl; }
    public void setProfilePicUrl(String profilePicUrl) { this.profilePicUrl = profilePicUrl; }

    public boolean isEmailVerified() { return isEmailVerified; }
    public void setEmailVerified(boolean emailVerified) { isEmailVerified = emailVerified; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    public UserPreferences getPreferences() { return preferences; }
    public void setPreferences(UserPreferences preferences) { this.preferences = preferences; }
}