package com.example.traveladvisor360.services;

import android.content.Context;

import com.example.traveladvisor360.callbacks.BookingCallback;
import com.example.traveladvisor360.models.Flight;
import com.example.traveladvisor360.models.Hotel;
import com.example.traveladvisor360.models.Experience;
import com.example.traveladvisor360.network.ApiResponse;
import com.example.traveladvisor360.utils.ApiClient;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingService {

    private static BookingService instance;
    private final Context context;

    private BookingService(Context context) {
        this.context = context.getApplicationContext();
    }

    public static synchronized BookingService getInstance(Context context) {
        if (instance == null) {
            instance = new BookingService(context.getApplicationContext());
        }
        return instance;
    }

    public void bookHotel(Hotel hotel, String checkIn, String checkOut, int guests, int rooms, BookingCallback callback) {
        JsonObject jsonRequest = new JsonObject();
        jsonRequest.addProperty("hotelId", hotel.getId());
        jsonRequest.addProperty("checkIn", checkIn);
        jsonRequest.addProperty("checkOut", checkOut);
        jsonRequest.addProperty("guests", guests);
        jsonRequest.addProperty("rooms", rooms);

        ApiClient.getInstance(context).getApiService().bookHotel(jsonRequest).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    callback.onSuccess();
                } else {
                    String errorMessage = "Hotel booking failed";
                    if (response.body() != null) {
                        errorMessage = response.body().getMessage();
                    }
                    callback.onError(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public void bookFlight(Flight flight, int passengers, String travelClass, BookingCallback callback) {
        JsonObject jsonRequest = new JsonObject();
        jsonRequest.addProperty("flightId", flight.getId());
        jsonRequest.addProperty("passengers", passengers);
        jsonRequest.addProperty("travelClass", travelClass);

        ApiClient.getInstance(context).getApiService().bookFlight(jsonRequest).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    callback.onSuccess();
                } else {
                    String errorMessage = "Flight booking failed";
                    if (response.body() != null) {
                        errorMessage = response.body().getMessage();
                    }
                    callback.onError(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public void bookExperience(Experience experience, String date, int participants, BookingCallback callback) {
        JsonObject jsonRequest = new JsonObject();
        jsonRequest.addProperty("experienceId", experience.getId());
        jsonRequest.addProperty("date", date);
        jsonRequest.addProperty("participants", participants);

        ApiClient.getInstance(context).getApiService().bookExperience(jsonRequest).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    callback.onSuccess();
                } else {
                    String errorMessage = "Experience booking failed";
                    if (response.body() != null) {
                        errorMessage = response.body().getMessage();
                    }
                    callback.onError(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }
}