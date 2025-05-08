package com.example.traveladvisor360.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.traveladvisor360.R;
import com.example.traveladvisor360.adapters.TravelCompanionAdapter;
import com.example.traveladvisor360.models.TravelCompanion;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class GroupTripPlanningFragment extends Fragment {

    private EditText destinationEdit;
    private DatePicker departureDatePicker;
    private DatePicker returnDatePicker;
    private EditText budgetEdit;
    private RecyclerView companionsRecyclerView;
    private FloatingActionButton addCompanionFab;
    private Button nextButton;
    private Button backButton;

    private List<TravelCompanion> companions = new ArrayList<>();
    private TravelCompanionAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_group_trip_planning, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        destinationEdit = view.findViewById(R.id.edit_destination);
        departureDatePicker = view.findViewById(R.id.date_picker_departure);
        returnDatePicker = view.findViewById(R.id.date_picker_return);
        budgetEdit = view.findViewById(R.id.edit_budget);
        companionsRecyclerView = view.findViewById(R.id.recycler_companions);
        addCompanionFab = view.findViewById(R.id.fab_add_companion);
        nextButton = view.findViewById(R.id.btn_next);
        backButton = view.findViewById(R.id.btn_back);

        // Set up RecyclerView
        adapter = new TravelCompanionAdapter(companions);
        companionsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        companionsRecyclerView.setAdapter(adapter);

        // Set click listeners
        addCompanionFab.setOnClickListener(v -> showAddCompanionDialog());

        nextButton.setOnClickListener(v -> {
            if (validateInputs()) {
                saveTripDetails();
                navigateToActivitiesSelection();
            }
        });

        backButton.setOnClickListener(v -> {
            // Navigate back to home
            Navigation.findNavController(view).navigateUp();
        });
    }

    private void showAddCompanionDialog() {
        // Show a dialog to add a companion
        // This would be implemented similar to your PlanningOptionsDialog
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

        if (companions.isEmpty()) {
            Snackbar.make(requireView(), "Please add at least one travel companion", Snackbar.LENGTH_SHORT).show();
            return false;
        }

        // Validate dates (same as in SoloTripPlanningFragment)

        return true;
    }

    private void saveTripDetails() {
        // Save trip details and companions to your data model or preferences
    }

    private void navigateToActivitiesSelection() {
        Bundle args = new Bundle();
        args.putString("destination", destinationEdit.getText().toString());
        args.putString("budget", budgetEdit.getText().toString());
        args.putInt("companions_count", companions.size());
        // Add other details as needed

        Navigation.findNavController(requireView())
                .navigate(R.id.action_destinationBudgetFragment_to_activitiesFragment, args);
    }
}