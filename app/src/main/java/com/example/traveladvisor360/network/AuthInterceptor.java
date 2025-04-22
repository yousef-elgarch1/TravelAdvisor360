package com.example.traveladvisor360.network;

import android.content.Context;

import com.example.traveladvisor360.models.AuthToken;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    private final TokenManager tokenManager;

    public AuthInterceptor(Context context) {
        this.tokenManager = TokenManager.getInstance(context);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        AuthToken token = tokenManager.getToken();
        if (token == null || token.getAccessToken() == null) {
            return chain.proceed(originalRequest);
        }

        Request.Builder builder = originalRequest.newBuilder()
                .header("Authorization", "Bearer " + token.getAccessToken());

        Request newRequest = builder.build();
        return chain.proceed(newRequest);
    }
}
