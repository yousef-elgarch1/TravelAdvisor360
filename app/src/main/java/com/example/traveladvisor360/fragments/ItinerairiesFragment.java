// 1. ItinerariesFragment.java
package com.example.traveladvisor360.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.traveladvisor360.R;
import com.example.traveladvisor360.activities.TripPlanningActivity;
import com.example.traveladvisor360.adapters.ItineraryAdapter;
import com.example.traveladvisor360.models.Itinerary;
import com.example.traveladvisor360.network.ApiResponse;
import com.example.traveladvisor360.network.ApiService;
import com.example.traveladvisor360.utils.ApiClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItinerairiesFragment extends Fragment {

    private RecyclerView rvItineraries;
    private ItineraryAdapter itineraryAdapter;
    private FloatingActionButton fabAddItinerary;
    private TextView tvEmptyState;
    private View loadingView;
    private ApiService apiService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_itineraries, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        apiService = ApiClient.getInstance(requireContext()).getApiService();

        initViews(view);
        setupRecyclerView();
        setupClickListeners();
        loadItineraries();
    }

    private void initViews(View view) {
        rvItineraries = view.findViewById(R.id.rv_itineraries);
        fabAddItinerary = view.findViewById(R.id.fab_add_itinerary);
        tvEmptyState = view.findViewById(R.id.tv_empty_state);
        loadingView = view.findViewById(R.id.loading_view);
    }

    private void setupRecyclerView() {
        itineraryAdapter = new ItineraryAdapter(requireContext());
        rvItineraries.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvItineraries.setAdapter(itineraryAdapter);

        // Set item click listener
        itineraryAdapter.setOnItineraryClickListener(itinerary -> {
            // Navigate to itinerary details
            Bundle args = new Bundle();
            args.putString("itinerary_id", itinerary.getId());
            // Navigate using NavController or Intent
        });
    }

    private void setupClickListeners() {
        fabAddItinerary.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), TripPlanningActivity.class);
            startActivity(intent);
        });
    }

    private void loadItineraries() {
        showLoading(true);

        // For demonstration, let's create mock data
        // In a real app, this would make an API call
        List<Itinerary> mockItineraries = createMockItineraries();

        if (mockItineraries.isEmpty()) {
            showEmptyState(true);
        } else {
            showEmptyState(false);
            itineraryAdapter.setItineraries(mockItineraries);
        }

        showLoading(false);

        // Real API call would look like this:
        /*
        apiService.getItineraries().enqueue(new Callback<ApiResponse<List<Itinerary>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Itinerary>>> call, Response<ApiResponse<List<Itinerary>>> response) {
                showLoading(false);
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    List<Itinerary> itineraries = response.body().getData();
                    if (itineraries.isEmpty()) {
                        showEmptyState(true);
                    } else {
                        showEmptyState(false);
                        itineraryAdapter.setItineraries(itineraries);
                    }
                } else {
                    showEmptyState(true);
                }
            }
            
            @Override
            public void onFailure(Call<ApiResponse<List<Itinerary>>> call, Throwable t) {
                showLoading(false);
                showEmptyState(true);
            }
        });
        */
    }

    private void showLoading(boolean show) {
        loadingView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void showEmptyState(boolean show) {
        tvEmptyState.setVisibility(show ? View.VISIBLE : View.GONE);
        rvItineraries.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private List<Itinerary> createMockItineraries() {
        List<Itinerary> itineraries = new ArrayList<>();

        // Paris Itinerary
        Itinerary parisItinerary = new Itinerary();
        parisItinerary.setId("1");
        parisItinerary.setTitle("Romantic Paris Getaway");
        parisItinerary.setDestination("Paris, France");

        // Set dates one month from now
        long currentTime = System.currentTimeMillis();
        Date startDate = new Date(currentTime + 30L * 24 * 60 * 60 * 1000);
        Date endDate = new Date(currentTime + 37L * 24 * 60 * 60 * 1000);

        parisItinerary.setStartDate(startDate);
        parisItinerary.setEndDate(endDate);
        parisItinerary.setTravelerCount(2);
        parisItinerary.setBudget(3000);

        itineraries.add(parisItinerary);

        // Tokyo Itinerary
        Itinerary tokyoItinerary = new Itinerary();
        tokyoItinerary.setId("2");
        tokyoItinerary.setTitle("Tokyo Adventure");
        tokyoItinerary.setDestination("Tokyo, Japan");

        // Set dates two months from now
        startDate = new Date(currentTime + 60L * 24 * 60 * 60 * 1000);
        endDate = new Date(currentTime + 70L * 24 * 60 * 60 * 1000);

        tokyoItinerary.setStartDate(startDate);
        tokyoItinerary.setEndDate(endDate);
        tokyoItinerary.setTravelerCount(1);
        tokyoItinerary.setBudget(4500);

        itineraries.add(tokyoItinerary);

        return itineraries;
    }
}