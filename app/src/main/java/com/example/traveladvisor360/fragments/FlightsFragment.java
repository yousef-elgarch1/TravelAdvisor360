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
import com.example.traveladvisor360.adapters.FlightAdapter;
import com.example.traveladvisor360.models.FlightOption;
import com.example.traveladvisor360.models.TripPlanningData;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlightsFragment extends Fragment {

    private RecyclerView flightsRecyclerView;
    private Button nextButton;
    private Button backButton;
    private TextView destinationTextView;

    private TripPlanningData tripData;
    private FlightAdapter adapter;
    private List<FlightOption> availableFlights = new ArrayList<>();
    private FlightOption selectedFlight;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tripData = TripPlanningData.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_flights, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        flightsRecyclerView = view.findViewById(R.id.recycler_flights);
        nextButton = view.findViewById(R.id.btn_next);
        backButton = view.findViewById(R.id.btn_back);
        destinationTextView = view.findViewById(R.id.text_destination);

        // Update destination text
        destinationTextView.setText(String.format("Flights to %s", tripData.getDestination()));

        // Load available flights
        loadFlightsForDestination();

        // Set up RecyclerView
        setupFlightsRecyclerView();

        // Set up click listeners
        nextButton.setOnClickListener(v -> {
            if (validateFlightSelection()) {
                saveSelectedFlight();
                navigateToHotelSelection();
            }
        });

        backButton.setOnClickListener(v -> {
            Navigation.findNavController(view).navigateUp();
        });
    }

    private void loadFlightsForDestination() {
        // In a real app, this would fetch flights from an API
        // For this example, we'll create mock data based on the destination
        availableFlights.clear();

        String destination = tripData.getDestination();

        // Add various flight options
        availableFlights.add(new FlightOption(
                "Economy", "AA123", "American Airlines",
                "JFK", destination, "08:00", "14:30",
                389.99, 1, false));

        availableFlights.add(new FlightOption(
                "Economy", "DL456", "Delta Airlines",
                "LGA", destination, "10:15", "16:45",
                425.50, 1, false));

        availableFlights.add(new FlightOption(
                "Business", "UA789", "United Airlines",
                "EWR", destination, "12:30", "19:00",
                789.99, 0, false));

        availableFlights.add(new FlightOption(
                "First Class", "BA246", "British Airways",
                "JFK", destination, "18:45", "08:15+1",
                1250.00, 0, false));

        // Check if we have a previously selected flight and mark it
        String previousSelection = tripData.getSelectedFlight();
        if (previousSelection != null) {
            for (FlightOption flight : availableFlights) {
                if (flight.getFlightNumber().equals(previousSelection)) {
                    flight.setSelected(true);
                    selectedFlight = flight;
                    break;
                }
            }
        }
    }

    private void setupFlightsRecyclerView() {
        adapter = new FlightAdapter(availableFlights);
        flightsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        flightsRecyclerView.setAdapter(adapter);

        // Set selection listener
        adapter.setOnFlightSelectedListener(flight -> {
            // Deselect previous selection
            if (selectedFlight != null) {
                selectedFlight.setSelected(false);
            }

            // Update selected flight
            selectedFlight = flight;
            selectedFlight.setSelected(true);
            adapter.notifyDataSetChanged();
        });
    }

    private boolean validateFlightSelection() {
        if (selectedFlight == null) {
            Snackbar.make(requireView(), "Please select a flight", Snackbar.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void saveSelectedFlight() {
        tripData.setSelectedFlight(selectedFlight.getFlightNumber());

        // Save flight details
        Map<String, Object> flightDetails = new HashMap<>();
        flightDetails.put("airline", selectedFlight.getAirline());
        flightDetails.put("class", selectedFlight.getTravelClass());
        flightDetails.put("departure", selectedFlight.getDepartureTime());
        flightDetails.put("arrival", selectedFlight.getArrivalTime());
        flightDetails.put("price", selectedFlight.getPrice());
        flightDetails.put("stops", selectedFlight.getStops());

        tripData.setFlightDetails(flightDetails);
    }

    private void navigateToHotelSelection() {
        Navigation.findNavController(requireView())
                .navigate(R.id.action_flightsFragment_to_hotelsFragment);
    }
}