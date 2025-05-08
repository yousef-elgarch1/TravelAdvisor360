package com.example.traveladvisor360.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.traveladvisor360.R;
import com.example.traveladvisor360.adapters.HotelAdapter;
import com.example.traveladvisor360.models.HotelOption;
import com.example.traveladvisor360.models.TripPlanningData;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HotelsFragment extends Fragment {

    private RecyclerView hotelsRecyclerView;
    private Button nextButton;
    private Button backButton;
    private TextView destinationTextView;

    private TripPlanningData tripData;
    private HotelAdapter adapter;
    private List<HotelOption> availableHotels = new ArrayList<>();
    private HotelOption selectedHotel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tripData = TripPlanningData.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_hotels, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        hotelsRecyclerView = view.findViewById(R.id.recycler_hotels);
        nextButton = view.findViewById(R.id.btn_next);
        backButton = view.findViewById(R.id.btn_back);
        destinationTextView = view.findViewById(R.id.text_destination);

        // Update destination text
        destinationTextView.setText(String.format("Hotels in %s", tripData.getDestination()));

        // Load available hotels
        loadHotelsForDestination();

        // Set up RecyclerView
        setupHotelsRecyclerView();

        // Set up click listeners
        nextButton.setOnClickListener(v -> {
            if (validateHotelSelection()) {
                saveSelectedHotel();
                navigateToSummary();
            }
        });

        backButton.setOnClickListener(v -> {
            Navigation.findNavController(view).navigateUp();
        });
    }

    private void loadHotelsForDestination() {
        // In a real app, this would fetch hotels from an API
        // For this example, we'll create mock data based on the destination
        availableHotels.clear();

        String destination = tripData.getDestination();

        // Add various hotel options
        availableHotels.add(new HotelOption(
                "Grand Plaza Hotel", destination,
                "Luxury hotel in the heart of the city",
                4.5, 199.99, 4, false));

        availableHotels.add(new HotelOption(
                "Comfort Inn", destination,
                "Affordable comfort with free breakfast",
                3.8, 89.99, 3, false));

        availableHotels.add(new HotelOption(
                "Royal Suites", destination,
                "All-suite luxury hotel with spa services",
                4.7, 249.99, 5, false));

        availableHotels.add(new HotelOption(
                "City View Apartments", destination,
                "Self-catering apartments with stunning views",
                4.2, 129.99, 4, false));

        // Check if we have a previously selected hotel and mark it
        String previousSelection = tripData.getSelectedHotel();
        if (previousSelection != null) {
            for (HotelOption hotel : availableHotels) {
                if (hotel.getName().equals(previousSelection)) {
                    hotel.setSelected(true);
                    selectedHotel = hotel;
                    break;
                }
            }
        }
    }

    private void setupHotelsRecyclerView() {
        adapter = new HotelAdapter(availableHotels);
        hotelsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        hotelsRecyclerView.setAdapter(adapter);

        // Set selection listener
        adapter.setOnHotelSelectedListener(hotel -> {
            // Deselect previous selection
            if (selectedHotel != null) {
                selectedHotel.setSelected(false);
            }

            // Update selected hotel
            selectedHotel = hotel;
            selectedHotel.setSelected(true);
            adapter.notifyDataSetChanged();
        });
    }

    private boolean validateHotelSelection() {
        if (selectedHotel == null) {
            Snackbar.make(requireView(), "Please select a hotel", Snackbar.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void saveSelectedHotel() {
        tripData.setSelectedHotel(selectedHotel.getName());

        // Save hotel details
        Map<String, Object> hotelDetails = new HashMap<>();
        hotelDetails.put("location", selectedHotel.getLocation());
        hotelDetails.put("description", selectedHotel.getDescription());
        hotelDetails.put("rating", selectedHotel.getRating());
        hotelDetails.put("price", selectedHotel.getPricePerNight());
        hotelDetails.put("stars", selectedHotel.getStars());

        tripData.setHotelDetails(hotelDetails);
    }

    private void navigateToSummary() {
        Navigation.findNavController(requireView())
                .navigate(R.id.action_hotelsFragment_to_tripSummaryFragment);
    }
}