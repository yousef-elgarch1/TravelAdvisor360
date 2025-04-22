package com.example.traveladvisor360.callbacks;

import com.example.traveladvisor360.models.Itinerary;

public interface RecommendationCallback {
    void onSuccess(Itinerary itinerary);
    void onError(String error);
}