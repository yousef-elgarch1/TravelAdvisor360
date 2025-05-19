// app/src/main/java/com/example/traveladvisor360/network/HotelApiService.java
package com.example.traveladvisor360.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface HotelApiService {
    @GET("v1/reference-data/locations/hotels/by-city")
    Call<HotelApiResponse> searchHotels(
            @Header("Authorization") String bearerToken,
            @Query("cityCode") String cityCode,
            @Query("radius") Integer radius // optional, in km
    );
}