package com.example.traveladvisor360.network;

import android.app.DownloadManager;

import com.example.traveladvisor360.models.Destination;
import com.example.traveladvisor360.models.Experience;
import com.example.traveladvisor360.models.Flight;
import com.example.traveladvisor360.models.Hotel;
import com.example.traveladvisor360.models.User;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("destinations")
    Call<ApiResponse<List<Destination>>> getDestinations();

    @GET("destinations/{id}")
    Call<ApiResponse<Destination>> getDestination(@Path("id") String destinationId);

    @GET("experiences")
    Call<ApiResponse<List<Experience>>> getExperiences();

    @GET("destinations/{id}/hotels")
    Call<ApiResponse<List<Hotel>>> getNearbyHotels(@Path("id") String destinationId);

    @GET("destinations/{id}/experiences")
    Call<ApiResponse<List<Experience>>> getNearbyExperiences(@Path("id") String destinationId);

    @GET("user/current")
    Call<ApiResponse<User>> getCurrentUser();

    @GET("search/destinations")
    Call<ApiResponse<List<Destination>>> searchDestinations(@Query("query") String query);

    // Booking methods
    @POST("bookings/hotel")
    Call<ApiResponse<Void>> bookHotel(@Body JsonObject bookingRequest);

    @POST("bookings/flight")
    Call<ApiResponse<Void>> bookFlight(@Body JsonObject bookingRequest);

    @POST("bookings/experience")
    Call<ApiResponse<Void>> bookExperience(@Body JsonObject bookingRequest);

    /**
     * Update user profile information
     * @param user User object with updated information
     * @return ApiResponse containing the updated User object
     */
    @PUT("users/profile")
    Call<ApiResponse<User>> updateUser(@Body User user);
}