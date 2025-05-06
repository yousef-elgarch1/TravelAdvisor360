package com.example.traveladvisor360.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.traveladvisor360.R;
import com.example.traveladvisor360.adapters.ItineraryDayAdapter;
import com.example.traveladvisor360.models.Itinerary;
import com.example.traveladvisor360.models.ItineraryDay;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class ItineraryDetaillsFragment extends Fragment {

    private ImageView ivItineraryImage;
    private TextView tvItineraryName;
    private TextView tvItineraryDescription;
    private TextView tvItineraryDuration;
    private TextView tvItineraryPrice;
    private RecyclerView rvItineraryDays;
    private MaterialButton btnBookItinerary;
    private View loadingView;
    private TextView tvEmptyState;

    private String itineraryId;
    private Itinerary currentItinerary;
    private ItineraryDayAdapter itineraryDayAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_itinerary_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            itineraryId = getArguments().getString("itinerary_id");
        }

        initViews(view);
        setupRecyclerView();
        setupClickListeners();
        loadItineraryDetails();
    }

    private void initViews(View view) {
        ivItineraryImage = view.findViewById(R.id.iv_itinerary_image);
        tvItineraryName = view.findViewById(R.id.tv_itinerary_name);
        tvItineraryDescription = view.findViewById(R.id.tv_itinerary_description);
        tvItineraryDuration = view.findViewById(R.id.tv_itinerary_duration);
        tvItineraryPrice = view.findViewById(R.id.tv_itinerary_price);
        rvItineraryDays = view.findViewById(R.id.rv_itinerary_days);
        btnBookItinerary = view.findViewById(R.id.btn_book_itinerary);
        loadingView = view.findViewById(R.id.loading_view);
        tvEmptyState = view.findViewById(R.id.tv_empty_state);
    }

    private void setupRecyclerView() {
        itineraryDayAdapter = new ItineraryDayAdapter(requireContext());
        rvItineraryDays.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvItineraryDays.setAdapter(itineraryDayAdapter);
        rvItineraryDays.setNestedScrollingEnabled(false);
    }

    private void setupClickListeners() {
        btnBookItinerary.setOnClickListener(v -> {
            // Navigate to booking screen
            Toast.makeText(requireContext(), "Booking functionality not implemented", Toast.LENGTH_SHORT).show();
        });
    }

    private void loadItineraryDetails() {
        if (itineraryId == null || itineraryId.isEmpty()) {
            showEmptyState("Itinerary not found");
            return;
        }

        showLoading(true);

        // In a real app, this would be an API call
        // For now, just use mock data
        // Simulating API delay
        Handler view = new Handler();
        view.postDelayed(() -> {
            // Create mock data
            currentItinerary = new Itinerary();
            currentItinerary.setId(itineraryId);
            currentItinerary.setName("Mediterranean Coastal Tour");
            currentItinerary.setDescription("Explore the beautiful Mediterranean coast with this 7-day itinerary covering key historical sites and natural wonders.");
            currentItinerary.setDuration("7 days");
            currentItinerary.setPrice("$1,200");
            currentItinerary.setImageUrl("https://example.com/mediterranean.jpg");

            // Create mock itinerary days
            List<ItineraryDay> days = new ArrayList<>();
            // Day 1
            ItineraryDay day1 = new ItineraryDay();
            day1.setDayNumber(1);
            day1.setTitle("Arrival in Barcelona");
            day1.setDescription("Arrive in Barcelona and check into your hotel. Explore the Gothic Quarter and enjoy dinner at a local tapas restaurant.");
            days.add(day1);

            // Day 2
            ItineraryDay day2 = new ItineraryDay();
            day2.setDayNumber(2);
            day2.setTitle("Barcelona Sightseeing");
            day2.setDescription("Visit Sagrada Familia, Park Güell, and other Gaudí masterpieces. Enjoy the beach in the afternoon.");
            days.add(day2);

            // Add more days...

            // Display the data
            displayItineraryDetails(currentItinerary, days);
            showLoading(false);
        }, 1000);
    }

    private void displayItineraryDetails(Itinerary itinerary, List<ItineraryDay> days) {
        tvItineraryName.setText(itinerary.getName());
        tvItineraryDescription.setText(itinerary.getDescription());
        tvItineraryDuration.setText(itinerary.getDuration());
        tvItineraryPrice.setText(itinerary.getPrice());

        // Load image
        if (itinerary.getImageUrl() != null && !itinerary.getImageUrl().isEmpty()) {
            Glide.with(this)
                    .load(itinerary.getImageUrl())
                    .placeholder(R.drawable.placeholder_experience)
                    .error(R.drawable.placeholder_experience)
                    .centerCrop()
                    .into(ivItineraryImage);
        }

        // Update adapter with days
        if (days != null && !days.isEmpty()) {
            itineraryDayAdapter.setDays(days);
        } else {
            showEmptyState("No itinerary details available");
        }
    }

    private void showLoading(boolean show) {
        loadingView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void showEmptyState(String message) {
        tvEmptyState.setText(message);
        tvEmptyState.setVisibility(View.VISIBLE);
        showLoading(false);
    }
}
