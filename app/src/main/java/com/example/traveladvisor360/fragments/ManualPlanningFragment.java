package com.example.traveladvisor360.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.traveladvisor360.R;
import com.example.traveladvisor360.adapters.ItineraryDayAdapter;
import com.example.traveladvisor360.models.ItineraryDay;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;
import java.util.List;

public class ManualPlanningFragment extends Fragment {

    private TextInputEditText etTitle;
    private TextInputEditText etStartDate;
    private TextInputEditText etEndDate;
    private RecyclerView rvItineraryDays;
    private ItineraryDayAdapter itineraryDayAdapter;
    private FloatingActionButton fabAddDay;
    private Button btnSave;
    private List<ItineraryDay> itineraryDays = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_manual_planning, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        setupRecyclerView();
        setupClickListeners();
    }

    private void initViews(View view) {
        etTitle = view.findViewById(R.id.et_title);
        etStartDate = view.findViewById(R.id.et_start_date);
        etEndDate = view.findViewById(R.id.et_end_date);
        rvItineraryDays = view.findViewById(R.id.rv_itinerary_days);
        fabAddDay = view.findViewById(R.id.fab_add_day);
        btnSave = view.findViewById(R.id.btn_save);
    }

    private void setupRecyclerView() {
        rvItineraryDays.setLayoutManager(new LinearLayoutManager(requireContext()));
        itineraryDayAdapter = new ItineraryDayAdapter(requireContext());
        rvItineraryDays.setAdapter(itineraryDayAdapter);

        // Set initial empty list
        itineraryDayAdapter.setDays(itineraryDays);
    }

    private void setupClickListeners() {
        fabAddDay.setOnClickListener(v -> addDay());
        btnSave.setOnClickListener(v -> saveItinerary());
    }

    private void addDay() {
        int dayNumber = itineraryDays.size() + 1;

        // Create new day using the existing constructor
        ItineraryDay newDay = new ItineraryDay("Day " + dayNumber);

        // Set additional properties after creation
        // Assuming you have setter methods in your ItineraryDay class
        try {
            // You may need to customize this based on your actual ItineraryDay class implementation
            if (newDay.getClass().getMethod("setDayNumber", int.class) != null) {
                newDay.getClass().getMethod("setDayNumber", int.class).invoke(newDay, dayNumber);
            }

            String startDateStr = etStartDate.getText().toString().trim();
            if (!startDateStr.isEmpty() &&
                    newDay.getClass().getMethod("setDate", String.class) != null) {
                newDay.getClass().getMethod("setDate", String.class).invoke(newDay, startDateStr);
            }
        } catch (Exception e) {
            // If reflection doesn't work or methods don't exist, just continue
        }

        itineraryDays.add(newDay);

        // Update adapter
        itineraryDayAdapter.setDays(itineraryDays);
    }

    private void saveItinerary() {
        String title = etTitle.getText().toString().trim();
        String startDate = etStartDate.getText().toString().trim();
        String endDate = etEndDate.getText().toString().trim();

        // Validate input
        if (title.isEmpty()) {
            etTitle.setError("Title is required");
            return;
        }

        if (startDate.isEmpty()) {
            etStartDate.setError("Start date is required");
            return;
        }

        if (endDate.isEmpty()) {
            etEndDate.setError("End date is required");
            return;
        }

        // Save itinerary to database or API
        // This would typically involve creating an Itinerary object and saving it
    }
}