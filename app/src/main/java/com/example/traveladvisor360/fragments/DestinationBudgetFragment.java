package com.example.traveladvisor360.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.traveladvisor360.R;
import com.example.traveladvisor360.models.TripPlanningData;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

public class DestinationBudgetFragment extends Fragment implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap googleMap;
    private EditText destinationEdit;
    private EditText budgetEdit;
    private Spinner currencySpinner;
    private Button searchButton;
    private Button nextButton;
    private Button backButton;

    private String tripType;
    private TripPlanningData tripData;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get trip type from arguments
        if (getArguments() != null) {
            tripType = getArguments().getString("trip_type", "solo");
        }

        // Initialize trip data
        tripData = TripPlanningData.getInstance();
        tripData.setTripType(tripType);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_destination_budget, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        mapView = view.findViewById(R.id.map_view);
        destinationEdit = view.findViewById(R.id.edit_destination);
        budgetEdit = view.findViewById(R.id.edit_budget);
        currencySpinner = view.findViewById(R.id.spinner_currency);
        searchButton = view.findViewById(R.id.btn_search);
        nextButton = view.findViewById(R.id.btn_next);
        backButton = view.findViewById(R.id.btn_back);

        // Initialize map
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        // Setup currency spinner
        setupCurrencySpinner();

        // Search button click listener
        searchButton.setOnClickListener(v -> {
            String destination = destinationEdit.getText().toString().trim();
            if (!destination.isEmpty()) {
                searchLocation(destination);
            } else {
                Snackbar.make(view, "Please enter a destination", Snackbar.LENGTH_SHORT).show();
            }
        });

        // Next button click listener
        nextButton.setOnClickListener(v -> {
            if (validateInputs()) {
                saveDestinationBudget();
                navigateToActivitiesSelection();
            }
        });

        // Back button click listener
        backButton.setOnClickListener(v -> {
            Navigation.findNavController(view).navigateUp();
        });
    }

    private void setupCurrencySpinner() {
        // Currency options
        String[] currencies = {"USD ($)", "EUR (€)", "GBP (£)", "JPY (¥)", "AUD (A$)"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                currencies);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        currencySpinner.setAdapter(adapter);
    }

    private void searchLocation(String location) {
        // In a real app, you would use geocoding to search for the location
        // For this example, we'll simulate finding the location

        // Example coordinates for popular destinations
        LatLng coordinates;

        switch (location.toLowerCase()) {
            case "paris":
                coordinates = new LatLng(48.8566, 2.3522);
                break;
            case "tokyo":
                coordinates = new LatLng(35.6762, 139.6503);
                break;
            case "new york":
                coordinates = new LatLng(40.7128, -74.0060);
                break;
            default:
                // Default to a random location
                coordinates = new LatLng(0, 0);
                Snackbar.make(requireView(), "Location not found, please try another", Snackbar.LENGTH_SHORT).show();
                return;
        }

        // Update the map
        if (googleMap != null) {
            googleMap.clear();
            googleMap.addMarker(new MarkerOptions().position(coordinates).title(location));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 10));
        }
    }

    private boolean validateInputs() {
        if (destinationEdit.getText().toString().trim().isEmpty()) {
            Snackbar.make(requireView(), "Please enter a destination", Snackbar.LENGTH_SHORT).show();
            return false;
        }

        if (budgetEdit.getText().toString().trim().isEmpty()) {
            Snackbar.make(requireView(), "Please enter your budget", Snackbar.LENGTH_SHORT).show();
            return false;
        }

        try {
            double budget = Double.parseDouble(budgetEdit.getText().toString().trim());
            if (budget <= 0) {
                Snackbar.make(requireView(), "Budget must be greater than zero", Snackbar.LENGTH_SHORT).show();
                return false;
            }
        } catch (NumberFormatException e) {
            Snackbar.make(requireView(), "Please enter a valid budget amount", Snackbar.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void saveDestinationBudget() {
        String destination = destinationEdit.getText().toString().trim();
        double budget = Double.parseDouble(budgetEdit.getText().toString().trim());
        String currency = currencySpinner.getSelectedItem().toString();

        // Save to trip data
        tripData.setDestination(destination);
        tripData.setBudget(budget);
        tripData.setCurrency(currency);
    }

    private void navigateToActivitiesSelection() {
        if ("group".equals(tripType) && !tripData.hasCompanions()) {
            // For group trips, navigate to companions selection if no companions added yet
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_destinationBudgetFragment_to_companionsFragment);
        } else {
            // Otherwise go directly to activities selection
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_destinationBudgetFragment_to_activitiesFragment);
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        // Set default map position
        LatLng defaultLocation = new LatLng(0, 0);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 2));
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
