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

import com.example.traveladvisor360.R;
import com.google.android.material.snackbar.Snackbar;

public class SoloTripPlanningFragment extends Fragment {

    private EditText destinationEdit;
    private DatePicker departureDatePicker;
    private DatePicker returnDatePicker;
    private EditText budgetEdit;
    private Button nextButton;
    private Button backButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_solo_trip_planning, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        destinationEdit = view.findViewById(R.id.edit_destination);
        departureDatePicker = view.findViewById(R.id.date_picker_departure);
        returnDatePicker = view.findViewById(R.id.date_picker_return);
        budgetEdit = view.findViewById(R.id.edit_budget);
        nextButton = view.findViewById(R.id.btn_next);
        backButton = view.findViewById(R.id.btn_back);

        // Set click listeners
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

    private boolean validateInputs() {
        if (destinationEdit.getText().toString().trim().isEmpty()) {
            Snackbar.make(requireView(), "Please enter a destination", Snackbar.LENGTH_SHORT).show();
            return false;
        }

        if (budgetEdit.getText().toString().trim().isEmpty()) {
            Snackbar.make(requireView(), "Please enter your budget", Snackbar.LENGTH_SHORT).show();
            return false;
        }

        // Validate dates
        int departureYear = departureDatePicker.getYear();
        int departureMonth = departureDatePicker.getMonth();
        int departureDay = departureDatePicker.getDayOfMonth();

        int returnYear = returnDatePicker.getYear();
        int returnMonth = returnDatePicker.getMonth();
        int returnDay = returnDatePicker.getDayOfMonth();

        // Simple validation to ensure return date is after departure date
        if (returnYear < departureYear ||
                (returnYear == departureYear && returnMonth < departureMonth) ||
                (returnYear == departureYear && returnMonth == departureMonth && returnDay < departureDay)) {
            Snackbar.make(requireView(), "Return date must be after departure date", Snackbar.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void saveTripDetails() {
        // Save trip details to your data model or preferences
        // This is where you'd handle the data persistence
    }

    private void navigateToActivitiesSelection() {
        Bundle args = new Bundle();
        args.putString("destination", destinationEdit.getText().toString());
        args.putString("budget", budgetEdit.getText().toString());
        // Add other details as needed

        Navigation.findNavController(requireView())
                .navigate(R.id.action_soloTripPlanningFragment_to_activitiesFragment, args);
    }
}