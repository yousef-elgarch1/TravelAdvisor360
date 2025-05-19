// app/src/main/java/com/example/traveladvisor360/network/AmadeusAuthService.java
package com.example.traveladvisor360.network;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AmadeusAuthService {
    @FormUrlEncoded
    @POST("v1/security/oauth2/token")
    Call<AuthResponse> getBearerToken(
            @Field("grant_type") String grantType,
            @Field("client_id") String clientId,
            @Field("client_secret") String clientSecret
    );
}