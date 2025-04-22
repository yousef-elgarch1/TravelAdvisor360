package com.example.traveladvisor360.callbacks;


public interface LocationCallback {
    void onLocationReceived(double latitude, double longitude);
    void onLocationError(String error);
}
