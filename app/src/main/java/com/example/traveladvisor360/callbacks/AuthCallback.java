package com.example.traveladvisor360.callbacks;


public interface AuthCallback<T> {
    void onSuccess(T result);
    void onError(String error);
}
