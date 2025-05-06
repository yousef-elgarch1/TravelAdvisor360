package com.example.traveladvisor360.services;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.traveladvisor360.callbacks.AuthCallback;
import com.example.traveladvisor360.models.User;

public class AuthService {
    private static final String PREF_NAME = "travel_advisor_prefs";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_PROFILE_PIC = "user_profile_pic";
    private static final String KEY_USER_EMAIL_VERIFIED = "user_email_verified";
    private static final String KEY_USER_CREATED_AT = "user_created_at";

    private static AuthService instance;
    private SharedPreferences preferences;

    private AuthService(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static AuthService getInstance(Context context) {
        if (instance == null) {
            instance = new AuthService(context.getApplicationContext());
        }
        return instance;
    }

    /**
     * Login user with email and password
     *
     * @param email User email
     * @param password User password
     * @param callback Callback to handle result
     */
    public void login(String email, String password, AuthCallback<User> callback) {
        // In a real app, this would make an API call to your backend
        // For now, we'll simulate a successful login after a short delay

        new Thread(() -> {
            try {
                // Simulate network delay
                Thread.sleep(1500);

                // Check for common test credentials or implement your actual authentication logic
                if (email.equals("test@example.com") && password.equals("password123")) {
                    User user = new User(
                            "1",
                            email,
                            "Test User",
                            null, // No profile pic URL for test user
                            true  // Email verified
                    );

                    // Save user data to SharedPreferences
                    saveUserData(user);

                    // Call success callback on main thread
                    android.os.Handler mainHandler = new android.os.Handler(android.os.Looper.getMainLooper());
                    mainHandler.post(() -> callback.onSuccess(user));
                } else {
                    // Call error callback on main thread
                    android.os.Handler mainHandler = new android.os.Handler(android.os.Looper.getMainLooper());
                    mainHandler.post(() -> callback.onError("Invalid email or password"));
                }
            } catch (InterruptedException e) {
                // Call error callback on main thread
                android.os.Handler mainHandler = new android.os.Handler(android.os.Looper.getMainLooper());
                mainHandler.post(() -> callback.onError("Authentication failed"));
            }
        }).start();
    }

    /**
     * Register new user
     *
     * @param email User email
     * @param password User password
     * @param name User name
     * @param callback Callback to handle result
     */
    public void register(String email, String password, String name, AuthCallback<User> callback) {
        // In a real app, this would make an API call to your backend
        // For now, we'll simulate a successful registration after a short delay

        new Thread(() -> {
            try {
                // Simulate network delay
                Thread.sleep(1500);

                // Check if email is already taken (in a real app)
                if (email.equals("taken@example.com")) {
                    // Call error callback on main thread
                    android.os.Handler mainHandler = new android.os.Handler(android.os.Looper.getMainLooper());
                    mainHandler.post(() -> callback.onError("Email already registered"));
                } else {
                    // Create new user with the constructor that matches your User class
                    User user = new User(
                            "2",
                            email,
                            name,
                            null, // No profile pic URL for new user
                            false // Email not verified yet
                    );

                    // Save user data to SharedPreferences
                    saveUserData(user);

                    // Call success callback on main thread
                    android.os.Handler mainHandler = new android.os.Handler(android.os.Looper.getMainLooper());
                    mainHandler.post(() -> callback.onSuccess(user));
                }
            } catch (InterruptedException e) {
                // Call error callback on main thread
                android.os.Handler mainHandler = new android.os.Handler(android.os.Looper.getMainLooper());
                mainHandler.post(() -> callback.onError("Registration failed"));
            }
        }).start();
    }

    /**
     * Check if a user is currently logged in
     *
     * @return true if a user is logged in, false otherwise
     */
    public boolean isLoggedIn() {
        return preferences.contains(KEY_USER_ID);
    }

    /**
     * Get current logged in user
     *
     * @return User object if logged in, null otherwise
     */
    public User getCurrentUser() {
        if (!isLoggedIn()) {
            return null;
        }

        String userId = preferences.getString(KEY_USER_ID, "");
        String userEmail = preferences.getString(KEY_USER_EMAIL, "");
        String userName = preferences.getString(KEY_USER_NAME, "");
        String profilePicUrl = preferences.getString(KEY_USER_PROFILE_PIC, null);
        boolean isEmailVerified = preferences.getBoolean(KEY_USER_EMAIL_VERIFIED, false);
        long createdAt = preferences.getLong(KEY_USER_CREATED_AT, System.currentTimeMillis());

        User user = new User(userId, userEmail, userName, profilePicUrl, isEmailVerified);
        user.setCreatedAt(createdAt);

        return user;
    }

    /**
     * Log out current user
     */
    public void logout(AuthCallback<Void> authCallback) {
        preferences.edit().clear().apply();
    }

    private void saveUserData(User user) {
        preferences.edit()
                .putString(KEY_USER_ID, user.getId())
                .putString(KEY_USER_EMAIL, user.getEmail())
                .putString(KEY_USER_NAME, user.getName())
                .putString(KEY_USER_PROFILE_PIC, user.getProfilePicUrl())
                .putBoolean(KEY_USER_EMAIL_VERIFIED, user.isEmailVerified())
                .putLong(KEY_USER_CREATED_AT, user.getCreatedAt())
                .apply();
    }
}