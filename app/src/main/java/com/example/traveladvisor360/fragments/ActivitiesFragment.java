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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.traveladvisor360.R;
import com.example.traveladvisor360.adapters.ActivityAdapter;
import com.example.traveladvisor360.models.TripActivity;
import com.example.traveladvisor360.models.TripPlanningData;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class ActivitiesFragment extends Fragment {

    private RecyclerView activitiesRecyclerView;
    private Button nextButton;
    private Button backButton;
    private TextView destinationTextView;

    private TripPlanningData tripData;
    private ActivityAdapter adapter;
    private List<TripActivity> availableActivities = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tripData = TripPlanningData.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_activities, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        activitiesRecyclerView = view.findViewById(R.id.recycler_activities);
        nextButton = view.findViewById(R.id.btn_next);
        backButton = view.findViewById(R.id.btn_back);
        destinationTextView = view.findViewById(R.id.text_destination);

        // Update destination text
        destinationTextView.setText(String.format("Activities in %s", tripData.getDestination()));

        // Load available activities
        loadActivitiesForDestination();

        // Set up RecyclerView
        setupActivitiesRecyclerView();

        // Set up click listeners
        nextButton.setOnClickListener(v -> {
            if (validateActivities()) {
                saveSelectedActivities();
                navigateToFlightsSelection();
            }
        });

        backButton.setOnClickListener(v -> {
            Navigation.findNavController(view).navigateUp();
        });
    }

    private void loadActivitiesForDestination() {
        // In a real app, this would fetch activities from an API
        // For this example, we'll create mock data based on the destination
        availableActivities.clear();

        String destination = tripData.getDestination().toLowerCase();

        // Add generic activities
        availableActivities.add(new TripActivity("City Tour", R.drawable.ic_city_tour, false));
        availableActivities.add(new TripActivity("Local Cuisine", R.drawable.ic_restaurant, false));
        availableActivities.add(new TripActivity("Cultural Experience", R.drawable.ic_museum, false));
        availableActivities.add(new TripActivity("Shopping", R.drawable.ic_shopping, false));

        // Add destination-specific activities
        switch (destination) {
            case "paris":
                availableActivities.add(new TripActivity("Eiffel Tower", R.drawable.ic_landmark, false));
                availableActivities.add(new TripActivity("Louvre Museum", R.drawable.ic_museum, false));
                availableActivities.add(new TripActivity("Seine River Cruise", R.drawable.ic_boat, false));
                availableActivities.add(new TripActivity("Montmartre Tour", R.drawable.ic_landmark, false));
                break;
            case "tokyo":
                availableActivities.add(new TripActivity("Tokyo Tower", R.drawable.ic_landmark, false));
                availableActivities.add(new TripActivity("Shibuya Crossing", R.drawable.ic_city, false));
                availableActivities.add(new TripActivity("Meiji Shrine", R.drawable.ic_temple, false));
                availableActivities.add(new TripActivity("Anime Tour", R.drawable.ic_entertainment, false));
                break;
            case "new york":
                availableActivities.add(new TripActivity("Statue of Liberty", R.drawable.ic_landmark, false));
                availableActivities.add(new TripActivity("Times Square", R.drawable.ic_city, false));
                availableActivities.add(new TripActivity("Central Park", R.drawable.ic_park, false));
                availableActivities.add(new TripActivity("Broadway Show", R.drawable.ic_entertainment, false));
                break;
            default:
                availableActivities.add(new TripActivity("Local Landmark", R.drawable.ic_landmark, false));
                availableActivities.add(new TripActivity("Nature Tour", R.drawable.ic_nature, false));
                availableActivities.add(new TripActivity("Local Entertainment", R.drawable.ic_entertainment, false));
        }

        // Mark previously selected activities
        for (TripActivity activity : availableActivities) {
            if (tripData.getSelectedActivities().contains(activity.getName())) {
                activity.setSelected(true);
            }
        }
    }

    private void setupActivitiesRecyclerView() {
        adapter = new ActivityAdapter(availableActivities);
        activitiesRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        activitiesRecyclerView.setAdapter(adapter);
    }

    private boolean validateActivities() {
        // Check if at least one activity is selected
        boolean anySelected = false;
        for (TripActivity activity : availableActivities) {
            if (activity.isSelected()) {
                anySelected = true;
                break;
            }
        }

        if (!anySelected) {
            Snackbar.make(requireView(), "Please select at least one activity", Snackbar.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void saveSelectedActivities() {
        // Clear previous selections
        tripData.getSelectedActivities().clear();

        // Add current selections
        for (TripActivity activity : availableActivities) {
            if (activity.isSelected()) {
                tripData.addActivity(activity.getName());
            }
        }
    }

    private void navigateToFlightsSelection() {
        Navigation.findNavController(requireView())
                .navigate(R.id.action_activitiesFragment_to_flightsFragment);
    }
}