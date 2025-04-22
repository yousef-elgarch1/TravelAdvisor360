package com.example.traveladvisor360.utils;

// SharedPreferencesManager.java

import android.content.Context;
import android.content.SharedPreferences;

import com.example.traveladvisor360.models.User;
import com.google.gson.Gson;

public class SharedPreferencesManager {
    private static final String PREF_NAME = "TravelPlanner360Prefs";
    private static final String KEY_USER = "user";
    private static final String KEY_DARK_MODE = "dark_mode";
    private static final String KEY_LANGUAGE = "language";
    private static final String KEY_FIRST_LAUNCH = "first_launch";

    private static SharedPreferencesManager instance;
    private final SharedPreferences preferences;
    private final Gson gson;

    private SharedPreferencesManager(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public static synchronized SharedPreferencesManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPreferencesManager(context.getApplicationContext());
        }
        return instance;
    }

    public void saveUser(User user) {
        String userJson = gson.toJson(user);
        preferences.edit().putString(KEY_USER, userJson).apply();
    }

    public User getUser() {
        String userJson = preferences.getString(KEY_USER, null);
        if (userJson != null) {
            return gson.fromJson(userJson, User.class);
        }
        return null;
    }

    public void clearUser() {
        preferences.edit().remove(KEY_USER).apply();
    }

    public void setDarkMode(boolean isDarkMode) {
        preferences.edit().putBoolean(KEY_DARK_MODE, isDarkMode).apply();
    }

    public boolean isDarkMode() {
        return preferences.getBoolean(KEY_DARK_MODE, false);
    }

    public void setLanguage(String language) {
        preferences.edit().putString(KEY_LANGUAGE, language).apply();
    }

    public String getLanguage() {
        return preferences.getString(KEY_LANGUAGE, "en");
    }

    public boolean isFirstLaunch() {
        return preferences.getBoolean(KEY_FIRST_LAUNCH, true);
    }

    public void setFirstLaunch(boolean firstLaunch) {
        preferences.edit().putBoolean(KEY_FIRST_LAUNCH, firstLaunch).apply();
    }

    public void clearAll() {
        preferences.edit().clear().apply();
    }
}