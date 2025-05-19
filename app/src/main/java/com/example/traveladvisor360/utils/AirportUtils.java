package com.example.traveladvisor360.utils;// AirportUtils.java
import android.content.Context;
import java.util.ArrayList;
import java.util.List;

public class AirportUtils {
    private static final List<AirportEntry> AIRPORTS = new ArrayList<>();

    static {
        AIRPORTS.add(new AirportEntry("Paris", "France", "CDG"));
        AIRPORTS.add(new AirportEntry("Lyon", "France", "LYS"));
        AIRPORTS.add(new AirportEntry("Marseille", "France", "MRS"));
        AIRPORTS.add(new AirportEntry("London", "United Kingdom", "LHR"));
        // Add more as needed
    }

    public static String findIataCode(Context context, String city, String country) {
        for (AirportEntry entry : AIRPORTS) {
            if (entry.city.equalsIgnoreCase(city) && entry.country.equalsIgnoreCase(country)) {
                return entry.iata;
            }
        }
        return null; // No fallback
    }
}