package com.example.traveladvisor360.network;

// file: com/example/traveladvisor360/network/GeoapifyService.java
import com.example.traveladvisor360.models.GeoapifyResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import com.example.traveladvisor360.models.GeoapifyResponse;

// file: app/src/main/java/com/example/traveladvisor360/network/GeoapifyService.java
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import com.example.traveladvisor360.models.GeoapifyResponse;

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
}