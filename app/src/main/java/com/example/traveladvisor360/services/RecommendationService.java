package com.example.traveladvisor360.services;

import android.os.Handler;
import android.os.Looper;

import com.example.traveladvisor360.callbacks.RecommendationCallback;
import com.example.traveladvisor360.models.Itinerary;
import com.example.traveladvisor360.models.ItineraryDay;
import com.example.traveladvisor360.models.ItineraryActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RecommendationService {
    private static RecommendationService instance;

    private RecommendationService() {
        // Private constructor to enforce singleton pattern
    }

    public static RecommendationService getInstance() {
        if (instance == null) {
            instance = new RecommendationService();
        }
        return instance;
    }

    /**
     * Generate an AI-powered itinerary based on user preferences
     *
     * @param destination The destination name
     * @param budget The budget amount
     * @param duration The trip duration in days
     * @param preferences List of user preferences (e.g., "Cultural", "Adventure")
     * @param callback Callback to handle the result
     */
    public void generateItinerary(String destination, int budget, int duration,
                                  List<String> preferences, RecommendationCallback callback) {
        // In a real app, this would make an API call to an AI service
        // For this example, we'll simulate a network delay and return a mock itinerary

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            try {
                Itinerary itinerary = createMockItinerary(destination, budget, duration, preferences);
                callback.onSuccess(itinerary);
            } catch (Exception e) {
                callback.onError("Failed to generate itinerary: " + e.getMessage());
            }
        }, 2000); // Simulate 2-second delay
    }

    /**
     * Creates a mock itinerary for demonstration purposes
     */
    private Itinerary createMockItinerary(String destination, int budget, int duration,
                                          List<String> preferences) {
        Itinerary itinerary = new Itinerary();

        // Set basic itinerary details
        itinerary.setId("ai-generated-" + System.currentTimeMillis());
        itinerary.setTitle(duration + "-Day Trip to " + destination);
        itinerary.setDestination(destination);

        // Set budget
        itinerary.setBudget(budget);

        // Set dates (current date to current date + duration)
        Calendar calendar = Calendar.getInstance();
        Date startDate = calendar.getTime();
        itinerary.setStartDate(startDate);

        calendar.add(Calendar.DAY_OF_MONTH, duration - 1);
        Date endDate = calendar.getTime();
        itinerary.setEndDate(endDate);

        // Generate itinerary days
        List<ItineraryDay> days = new ArrayList<>();
        calendar.setTime(startDate); // Reset to start date

        for (int i = 1; i <= duration; i++) {
            ItineraryDay day = new ItineraryDay();
            day.setId("day" + i);
            day.setDayNumber(i);
            day.setDate(calendar.getTime());

            // Add example activities based on preferences
            List<ItineraryActivity> activities = new ArrayList<>();

            // Morning activity
            ItineraryActivity morningActivity = new ItineraryActivity();
            morningActivity.setId("act" + i + "-1");
            morningActivity.setTitle(generateActivityTitle(preferences, "morning"));

            Calendar activityTime = (Calendar) calendar.clone();
            activityTime.set(Calendar.HOUR_OF_DAY, 9);
            activityTime.set(Calendar.MINUTE, 0);
            morningActivity.setStartTime(activityTime.getTime());

            activityTime.set(Calendar.HOUR_OF_DAY, 12);
            activityTime.set(Calendar.MINUTE, 0);
            morningActivity.setEndTime(activityTime.getTime());

            morningActivity.setType(getActivityType(preferences));
            morningActivity.setLocation(destination);
            activities.add(morningActivity);

            // Afternoon activity
            ItineraryActivity afternoonActivity = new ItineraryActivity();
            afternoonActivity.setId("act" + i + "-2");
            afternoonActivity.setTitle(generateActivityTitle(preferences, "afternoon"));

            activityTime = (Calendar) calendar.clone();
            activityTime.set(Calendar.HOUR_OF_DAY, 14);
            activityTime.set(Calendar.MINUTE, 0);
            afternoonActivity.setStartTime(activityTime.getTime());

            activityTime.set(Calendar.HOUR_OF_DAY, 17);
            activityTime.set(Calendar.MINUTE, 0);
            afternoonActivity.setEndTime(activityTime.getTime());

            afternoonActivity.setType(getActivityType(preferences));
            afternoonActivity.setLocation(destination);
            activities.add(afternoonActivity);

            // Evening activity
            ItineraryActivity eveningActivity = new ItineraryActivity();
            eveningActivity.setId("act" + i + "-3");
            eveningActivity.setTitle(generateActivityTitle(preferences, "evening"));

            activityTime = (Calendar) calendar.clone();
            activityTime.set(Calendar.HOUR_OF_DAY, 19);
            activityTime.set(Calendar.MINUTE, 0);
            eveningActivity.setStartTime(activityTime.getTime());

            activityTime.set(Calendar.HOUR_OF_DAY, 21);
            activityTime.set(Calendar.MINUTE, 0);
            eveningActivity.setEndTime(activityTime.getTime());

            eveningActivity.setType("RESTAURANT");
            eveningActivity.setLocation(destination);
            activities.add(eveningActivity);

            day.setActivities(activities);
            days.add(day);

            // Move to next day
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        itinerary.setDays(days);
        itinerary.setNotes("This is an AI-generated itinerary based on your preferences. Feel free to customize it!");

        return itinerary;
    }

    private String generateActivityTitle(List<String> preferences, String timeOfDay) {
        // Generate activity titles based on preferences
        if (preferences.contains("Cultural")) {
            if ("morning".equals(timeOfDay)) {
                return "Visit local museum";
            } else if ("afternoon".equals(timeOfDay)) {
                return "Explore historic site";
            } else {
                return "Traditional dinner experience";
            }
        } else if (preferences.contains("Adventure")) {
            if ("morning".equals(timeOfDay)) {
                return "Hiking expedition";
            } else if ("afternoon".equals(timeOfDay)) {
                return "Water sports activity";
            } else {
                return "Dinner with scenic views";
            }
        } else if (preferences.contains("Relaxation")) {
            if ("morning".equals(timeOfDay)) {
                return "Spa treatment";
            } else if ("afternoon".equals(timeOfDay)) {
                return "Beach relaxation";
            } else {
                return "Quiet dinner by the sea";
            }
        } else if (preferences.contains("Food Tourism")) {
            if ("morning".equals(timeOfDay)) {
                return "Local market tour";
            } else if ("afternoon".equals(timeOfDay)) {
                return "Cooking class";
            } else {
                return "Food tasting tour";
            }
        } else {
            // Default activities
            if ("morning".equals(timeOfDay)) {
                return "City tour";
            } else if ("afternoon".equals(timeOfDay)) {
                return "Shopping";
            } else {
                return "Dinner at recommended restaurant";
            }
        }
    }

    private String getActivityType(List<String> preferences) {
        if (preferences.contains("Cultural")) {
            return "ATTRACTION";
        } else if (preferences.contains("Adventure")) {
            return "ADVENTURE";
        } else if (preferences.contains("Relaxation")) {
            return "RELAXATION";
        } else if (preferences.contains("Food Tourism")) {
            return "RESTAURANT";
        } else {
            return "OTHER";
        }
    }
}