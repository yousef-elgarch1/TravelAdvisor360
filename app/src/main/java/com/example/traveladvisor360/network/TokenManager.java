package com.example.traveladvisor360.network;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.traveladvisor360.models.AuthToken;
import com.google.gson.Gson;

public class TokenManager {
    private static final String PREF_NAME = "AuthTokenPrefs";
    private static final String KEY_TOKEN = "auth_token";

    private static TokenManager instance;
    private final SharedPreferences preferences;
    private final Gson gson;

    private TokenManager(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public static synchronized TokenManager getInstance(Context context) {
        if (instance == null) {
            instance = new TokenManager(context.getApplicationContext());
        }
        return instance;
    }

    public void saveToken(AuthToken token) {
        String tokenJson = gson.toJson(token);
        preferences.edit().putString(KEY_TOKEN, tokenJson).apply();
    }

    public AuthToken getToken() {
        String tokenJson = preferences.getString(KEY_TOKEN, null);
        if (tokenJson == null) {
            return null;
        }
        return gson.fromJson(tokenJson, AuthToken.class);
    }

    public void clearToken() {
        preferences.edit().remove(KEY_TOKEN).apply();
    }
}
