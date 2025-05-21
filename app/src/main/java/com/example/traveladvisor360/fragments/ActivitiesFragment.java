package com.example.traveladvisor360.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.traveladvisor360.R;
import com.example.traveladvisor360.models.GeoapifyResponse;
import com.example.traveladvisor360.models.TripPlanningData;
import java.util.*;
import retrofit2.*;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActivitiesFragment extends Fragment {
    private EditText editName, editLocation, editDate, editTime;
    private Button btnAdd, btnNext;
    private ListView listActivities;
    private ArrayAdapter<String> activitiesAdapter;
    private List<String> activitiesList = new ArrayList<>();
    private ArrayAdapter<String> locationAdapter;
    private List<GeoapifyResponse.Feature> locationFeatures = new ArrayList<>();
    private com.example.traveladvisor360.network.GeoapifyService geoapifyService;
    private static final String GEOAPIFY_BASE_URL = "https://api.geoapify.com/";
    private static final String GEOAPIFY_API_KEY = "b77658ed5cce424b9226636d47a85ea5";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activities, container, false);

        // Find views
        editName = view.findViewById(R.id.edit_activity_name);
        editLocation = view.findViewById(R.id.edit_location);
        editDate = view.findViewById(R.id.edit_activity_date);
        editTime = view.findViewById(R.id.edit_activity_time);
        btnAdd = view.findViewById(R.id.btn_add_activity);
        btnNext = view.findViewById(R.id.btn_next);
        listActivities = view.findViewById(R.id.list_activities);

        // List setup
        activitiesAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, activitiesList);
        listActivities.setAdapter(activitiesAdapter);

        // Setup location autocomplete
        if (editLocation instanceof AutoCompleteTextView) {
            locationAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, new ArrayList<>());
            ((AutoCompleteTextView) editLocation).setAdapter(locationAdapter);

            Retrofit geoapifyRetrofit = new Retrofit.Builder()
                    .baseUrl(GEOAPIFY_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            geoapifyService = geoapifyRetrofit.create(com.example.traveladvisor360.network.GeoapifyService.class);

            editLocation.addTextChangedListener(new TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
                @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() >= 2) fetchPlaceSuggestions(s.toString());
                }
                @Override public void afterTextChanged(Editable s) { }
            });

            ((AutoCompleteTextView) editLocation).setOnItemClickListener((parent, v1, position, id) -> {
                if (position < locationFeatures.size()) {
                    GeoapifyResponse.Feature feature = locationFeatures.get(position);
                    String name = feature.properties.name;
                    String address = feature.properties.formatted;
                    String display = !TextUtils.isEmpty(name) ? name : address;
                    editLocation.setText(display);
                }
            });
        }

        // Date and time pickers
        editDate.setOnClickListener(v -> showDatePicker());
        editTime.setOnClickListener(v -> showTimePicker());

        // Add Activity button
        btnAdd.setOnClickListener(v -> {
            String name = editName.getText().toString().trim();
            String location = editLocation.getText().toString().trim();
            String date = editDate.getText().toString().trim();
            String time = editTime.getText().toString().trim();
            if (name.isEmpty() || location.isEmpty() || date.isEmpty() || time.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }
            String summary = name + " at " + location + " on " + date + " " + time;
            activitiesList.add(summary);
            activitiesAdapter.notifyDataSetChanged();
            TripPlanningData.getInstance().getSelectedActivities().add(summary);

            // Reset fields
            editName.setText("");
            editLocation.setText("");
            editDate.setText("");
            editTime.setText("");
        });

        // Next button
        btnNext.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_activitiesFragment_to_tripSummaryFragment);
        });

        return view;
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
            requireContext(),
            (view, year, month, dayOfMonth) -> {
                String date = String.format("%02d/%02d/%d", dayOfMonth, month + 1, year);
                editDate.setText(date);
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(
            requireContext(),
            (view, hourOfDay, minute) -> {
                String time = String.format("%02d:%02d", hourOfDay, minute);
                editTime.setText(time);
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        );
        timePickerDialog.show();
    }

    private void fetchPlaceSuggestions(String query) {
        // Implement place suggestions fetching using Geoapify API
        // This is a placeholder for the actual implementation
    }
}