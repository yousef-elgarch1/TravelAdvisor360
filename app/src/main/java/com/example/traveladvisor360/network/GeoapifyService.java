package com.example.traveladvisor360.network;

// file: com/example/traveladvisor360/network/GeoapifyService.java
import com.example.traveladvisor360.models.GeoapifyResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

// file: app/src/main/java/com/example/traveladvisor360/network/GeoapifyService.java


//public interface GeoapifyService {
//    @GET("v1/geocode/autocomplete")
//    Call<GeoapifyResponse> autocompleteCities(
//            @Query("apiKey") String apiKey,
//            @Query("text") String text,
//            @Query("limit") int limit,
//            @Query("type") String type // Add this parameter
//    );
//}

public interface GeoapifyService {
    @GET("v1/geocode/autocomplete")
    Call<GeoapifyResponse> autocompleteCities(
            @Query("text") String text,
            @Query("apiKey") String apiKey
    );

    @GET("v1/place/autocomplete")
    Call<GeoapifyResponse> autocompletePlaces(
            @Query("text") String query,
            @Query("apiKey") String apiKey,
            @Query("categories") String categories, // e.g., "restaurant,beach,park"
            @Query("limit") int limit,
            @Query("filter") String filter
    );
}