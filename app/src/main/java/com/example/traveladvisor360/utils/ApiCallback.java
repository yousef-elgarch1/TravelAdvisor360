package com.example.traveladvisor360.utils;

/**
 * Callback interface for API requests.
 * @param <T> The type of data expected in the response.
 */
public interface ApiCallback<T> {

    /**
     * Called when the API request is successful.
     * @param result The data returned from the API.
     */
    void onSuccess(T result);

    /**
     * Called when the API request fails.
     * @param error The error message.
     */
    void onError(String error);
}