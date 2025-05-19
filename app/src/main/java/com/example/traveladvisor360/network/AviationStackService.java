package com.example.traveladvisor360.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AviationStackService {
    @GET("v1/cities")
    Call<AviationStackResponse> getCityIata(
            @Query("access_key") String apiKey,
            @Query("search") String cityName
    );
}