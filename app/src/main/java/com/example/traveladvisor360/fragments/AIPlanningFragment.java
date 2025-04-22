package com.example.traveladvisor360.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.traveladvisor360.R;
import com.example.traveladvisor360.adapters.ItineraryAdapter;
import com.example.traveladvisor360.callbacks.RecommendationCallback;
import com.example.traveladvisor360.models.Itinerary;
import com.example.traveladvisor360.services.RecommendationService;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.slider.Slider;

import java.util.ArrayList;
import java.util.List;

public class AIPlanningFragment extends Fragment {

    private EditText etDestination;
    private Slider sliderBudget;
    private TextView tvBudgetValue;
    private Slider sliderDuration;
    private TextView tvDurationValue;
    private CheckBox cbCultural;
    private CheckBox cbAdventure;
    private CheckBox cbRelaxation;
    private CheckBox cbFoodTourism;
    private Button btnGenerateItinerary;
    private ProgressBar progressBar;
    private RecyclerView rvGeneratedItinerary;
    private ItineraryAdapter itineraryAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ai_planning, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        setupListeners();
        setupRecyclerView();
    }

    private void initViews(View view) {
        etDestination = view.findViewById(R.id.et_destination);
        sliderBudget = view.findViewById(R.id.slider_budget);
        tvBudgetValue = view.findViewById(R.id.tv_budget_value);
        sliderDuration = view.findViewById(R.id.slider_duration);
        tvDurationValue = view.findViewById(R.id.tv_duration_value);
        cbCultural = view.findViewById(R.id.cb_cultural);
        cbAdventure = view.findViewById(R.id.cb_adventure);
        cbRelaxation = view.findViewById(R.id.cb_relaxation);
        cbFoodTourism = view.findViewById(R.id.cb_food_tourism);
        btnGenerateItinerary = view.findViewById(R.id.btn_generate_itinerary);
        progressBar = view.findViewById(R.id.progress_bar);
        rvGeneratedItinerary = view.findViewById(R.id.rv_generated_itinerary);
    }

    private void setupListeners() {
        sliderBudget.addOnChangeListener((slider, value, fromUser) -> {
            tvBudgetValue.setText(String.format("$%.0f", value));
        });

        sliderDuration.addOnChangeListener((slider, value, fromUser) -> {
            tvDurationValue.setText(String.format("%.0f days", value));
        });

        btnGenerateItinerary.setOnClickListener(v -> {
            if (validateInputs()) {
                generateAIItinerary();
            }
        });
    }

    private boolean validateInputs() {
        if (etDestination.getText().toString().isEmpty()) {
            etDestination.setError("Please enter a destination");
            return false;
        }

        if (!cbCultural.isChecked() && !cbAdventure.isChecked()
                && !cbRelaxation.isChecked() && !cbFoodTourism.isChecked()) {
            Toast.makeText(requireContext(), "Please select at least one preference",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void setupRecyclerView() {
        itineraryAdapter = new ItineraryAdapter(requireContext());
        rvGeneratedItinerary.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvGeneratedItinerary.setAdapter(itineraryAdapter);
    }

    private void generateAIItinerary() {
        progressBar.setVisibility(View.VISIBLE);
        btnGenerateItinerary.setEnabled(false);

        List<String> preferences = new ArrayList<>();
        if (cbCultural.isChecked()) preferences.add("Cultural");
        if (cbAdventure.isChecked()) preferences.add("Adventure");
        if (cbRelaxation.isChecked()) preferences.add("Relaxation");
        if (cbFoodTourism.isChecked()) preferences.add("Food Tourism");

        String destination = etDestination.getText().toString();
        int budget = (int) sliderBudget.getValue();
        int duration = (int) sliderDuration.getValue();

        RecommendationService.getInstance().generateItinerary(
                destination, budget, duration, preferences, new RecommendationCallback() {
                    @Override
                    public void onSuccess(Itinerary itinerary) {
                        requireActivity().runOnUiThread(() -> {
                            progressBar.setVisibility(View.GONE);
                            btnGenerateItinerary.setEnabled(true);
                            displayGeneratedItinerary(itinerary);
                        });
                    }

                    @Override
                    public void onError(String error) {
                        requireActivity().runOnUiThread(() -> {
                            progressBar.setVisibility(View.GONE);
                            btnGenerateItinerary.setEnabled(true);
                            Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show();
                        });
                    }
                });
    }

    private void displayGeneratedItinerary(Itinerary itinerary) {
        rvGeneratedItinerary.setVisibility(View.VISIBLE);
        itineraryAdapter.setItineraryDays(itinerary.getDays());
        showSaveItineraryDialog(itinerary);
    }

    private void showSaveItineraryDialog(Itinerary itinerary) {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Save Itinerary")
                .setMessage("Would you like to save this itinerary to your account?")
                .setPositiveButton("Save", (dialog, which) -> {
                    saveItinerary(itinerary);
                })
                .setNegativeButton("Later", null)
                .show();
    }

    private void saveItinerary(Itinerary itinerary) {
        Toast.makeText(requireContext(), "Itinerary saved successfully!",
                Toast.LENGTH_SHORT).show();}}