package com.example.traveladvisor360.callbacks;



public interface ChatbotCallback {
    void onResponse(String response);
    void onError(String error);
}