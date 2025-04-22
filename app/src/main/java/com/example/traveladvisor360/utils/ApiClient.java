package com.example.traveladvisor360.utils;

import android.content.Context;

import com.example.traveladvisor360.models.Destination;
import com.example.traveladvisor360.models.Experience;
import com.example.traveladvisor360.models.Hotel;
import com.example.traveladvisor360.network.ApiService;
import com.example.traveladvisor360.network.ApiResponse;
import com.example.traveladvisor360.network.AuthInterceptor;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static ApiClient instance;
    private final Retrofit retrofit;
    private final ApiService apiService;

    private ApiClient(Context context) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(new AuthInterceptor(context))
                .addInterceptor(loggingInterceptor)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.traveladvisor360.com/") // Replace with actual API base URL
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    public static synchronized ApiClient getInstance(Context context) {
        if (instance == null) {
            instance = new ApiClient(context.getApplicationContext());
        }
        return instance;
    }

    public static synchronized ApiClient getInstance() {
        if (instance == null) {
            throw new IllegalStateException("ApiClient not initialized. Call getInstance(Context) first.");
        }
        return instance;
    }

    public ApiService getApiService() {
        return apiService;
    }

    public void getDestinations(ApiCallback<List<Destination>> callback) {
        Call<ApiResponse<List<Destination>>> call = apiService.getDestinations();
        call.enqueue(new Callback<ApiResponse<List<Destination>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Destination>>> call, Response<ApiResponse<List<Destination>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    callback.onSuccess(response.body().getData());
                } else {
                    String error = response.body() != null ? response.body().getMessage() : "Unknown error";
                    callback.onError(error);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Destination>>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public void getDestination(String destinationId, ApiCallback<Destination> callback) {
        Call<ApiResponse<Destination>> call = apiService.getDestination(destinationId);
        call.enqueue(new Callback<ApiResponse<Destination>>() {
            @Override
            public void onResponse(Call<ApiResponse<Destination>> call, Response<ApiResponse<Destination>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    callback.onSuccess(response.body().getData());
                } else {
                    String error = response.body() != null ? response.body().getMessage() : "Unknown error";
                    callback.onError(error);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Destination>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public void getExperiences(ApiCallback<List<Experience>> callback) {
        Call<ApiResponse<List<Experience>>> call = apiService.getExperiences();
        call.enqueue(new Callback<ApiResponse<List<Experience>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Experience>>> call, Response<ApiResponse<List<Experience>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    callback.onSuccess(response.body().getData());
                } else {
                    String error = response.body() != null ? response.body().getMessage() : "Unknown error";
                    callback.onError(error);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Experience>>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public void getNearbyHotels(String destinationId, ApiCallback<List<Hotel>> callback) {
        Call<ApiResponse<List<Hotel>>> call = apiService.getNearbyHotels(destinationId);
        call.enqueue(new Callback<ApiResponse<List<Hotel>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Hotel>>> call, Response<ApiResponse<List<Hotel>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    callback.onSuccess(response.body().getData());
                } else {
                    String error = response.body() != null ? response.body().getMessage() : "Unknown error";
                    callback.onError(error);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Hotel>>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public void getNearbyExperiences(String destinationId, ApiCallback<List<Experience>> callback) {
        Call<ApiResponse<List<Experience>>> call = apiService.getNearbyExperiences(destinationId);
        call.enqueue(new Callback<ApiResponse<List<Experience>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Experience>>> call, Response<ApiResponse<List<Experience>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    callback.onSuccess(response.body().getData());
                } else {
                    String error = response.body() != null ? response.body().getMessage() : "Unknown error";
                    callback.onError(error);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Experience>>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public void searchDestinations(String query, ApiCallback<List<Destination>> callback) {
        Call<ApiResponse<List<Destination>>> call = apiService.searchDestinations(query);
        call.enqueue(new Callback<ApiResponse<List<Destination>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Destination>>> call, Response<ApiResponse<List<Destination>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    callback.onSuccess(response.body().getData());
                } else {
                    String error = response.body() != null ? response.body().getMessage() : "Unknown error";
                    callback.onError(error);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Destination>>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }
}