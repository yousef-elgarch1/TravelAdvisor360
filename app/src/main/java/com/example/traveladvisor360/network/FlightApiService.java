// app/src/main/java/com/example/traveladvisor360/network/FlightApiService.java
package com.example.traveladvisor360.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface FlightApiService {
    @GET("v2/shopping/flight-offers")
    Call<FlightApiResponse> searchFlights(
            @Header("Authorization") String bearerToken,
            @Query("originLocationCode") String origin,
            @Query("destinationLocationCode") String destination,
            @Query("departureDate") String departureDate,
            @Query("adults") int adults
    );
}