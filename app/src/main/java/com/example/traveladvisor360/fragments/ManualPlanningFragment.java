package com.example.traveladvisor360.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

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
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ManualPlanningFragment extends Fragment {

    private TextInputEditText etTitle;
    private TextInputEditText etStartDate;
    private TextInputEditText etEndDate;
    private RecyclerView rvItineraryDays;
    private ItineraryDayAdapter itineraryDayAdapter;
    private FloatingActionButton fabAddDay;
    private Button btnSave;
    private List<ItineraryDay> itineraryDays = new ArrayList<>();

    // Add fields for map functionality
    private AutoCompleteTextView editDestination;
    private AutoCompleteTextView editDeparture;
    private ImageButton btnDestinationMap;
    private ImageButton btnDepartureMap;
    private Spinner spinnerCurrency;

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

        // Initialize map-related components
        initMapButtons(view);
        initDatePickers(view);
        initCurrencySpinner(view);
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

    //===================== NEW MAP FUNCTIONALITY METHODS =====================

    /**
     * Initialize the map button click handlers
     */
    private void initMapButtons(View view) {
        // Initialize views - find them by ID in the layout
        try {
            editDestination = view.findViewById(R.id.edit_destination);
            editDeparture = view.findViewById(R.id.edit_departure);
            btnDestinationMap = view.findViewById(R.id.btn_destination_map);
            btnDepartureMap = view.findViewById(R.id.btn_departure_map);

            // Log whether we found the buttons
            if (btnDestinationMap == null) {
                Log.e("MapDebug", "Destination map button not found in layout!");
            }

            if (btnDepartureMap == null) {
                Log.e("MapDebug", "Departure map button not found in layout!");
            }

            // Set click listeners for map buttons if they exist
            if (btnDestinationMap != null) {
                btnDestinationMap.setOnClickListener(v -> {
                    Log.d("MapDebug", "Destination map button clicked!");
                    openMapSelection("Select Destination", true);
                });
            }

            if (btnDepartureMap != null) {
                btnDepartureMap.setOnClickListener(v -> {
                    Log.d("MapDebug", "Departure map button clicked!");
                    openMapSelection("Select Departure", false);
                });
            }
        } catch (Exception e) {
            Log.e("MapDebug", "Error initializing map buttons: " + e.getMessage());
        }
    }

    /**
     * Opens the map selection dialog
     */
    private void openMapSelection(String title, boolean isDestination) {
        try {
            MapSelectionFragment dialog = MapSelectionFragment.newInstance(title, isDestination);

            dialog.setOnLocationSelectedListener((locationName, isForDestination) -> {
                if (isForDestination) {
                    if (editDestination != null) {
                        editDestination.setText(locationName);
                    }
                } else {
                    if (editDeparture != null) {
                        editDeparture.setText(locationName);
                    }
                }
            });

            dialog.show(getParentFragmentManager(), "mapSelection");
        } catch (Exception e) {
            Log.e("MapDebug", "Error opening map selection: " + e.getMessage());
        }
    }

    /**
     * Initialize date picker dialogs for the date fields
     */
    private void initDatePickers(View view) {
        try {
            // Find date fields by ID - this is optional and you can skip if these already have pickers
            TextInputEditText startDateField = view.findViewById(R.id.edit_start_date);
            TextInputEditText returnDateField = view.findViewById(R.id.edit_return_date);

            // If date fields were found, add click listeners
            if (startDateField != null) {
                startDateField.setOnClickListener(v -> showDatePicker(startDateField));
            }

            if (returnDateField != null) {
                returnDateField.setOnClickListener(v -> showDatePicker(returnDateField));
            }
        } catch (Exception e) {
            Log.e("MapDebug", "Error initializing date pickers: " + e.getMessage());
        }
    }

    /**
     * Shows a date picker dialog and updates the field with the selected date
     */
    private void showDatePicker(TextInputEditText dateField) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Format the date as desired
                    String selectedDate = String.format(Locale.getDefault(),
                            "%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear);
                    dateField.setText(selectedDate);
                },
                year, month, day);

        // Set minimum date to today
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    /**
     * Initialize the currency spinner with common currencies
     */
    private void initCurrencySpinner(View view) {
        try {
            spinnerCurrency = view.findViewById(R.id.spinner_currency);

            if (spinnerCurrency != null) {
                // Create an array of common currencies
                String[] currencies = new String[] {
                        "USD - US Dollar",
                        "EUR - Euro",
                        "GBP - British Pound",
                        "JPY - Japanese Yen",
                        "CAD - Canadian Dollar",
                        "AUD - Australian Dollar",
                        "CHF - Swiss Franc",
                        "CNY - Chinese Yuan",
                        "INR - Indian Rupee",
                        "MAD - Moroccan Dirham"
                };

                // Create an adapter for the spinner
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        requireContext(), android.R.layout.simple_spinner_item, currencies);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // Apply the adapter to the spinner
                spinnerCurrency.setAdapter(adapter);
            }
        } catch (Exception e) {
            Log.e("MapDebug", "Error initializing currency spinner: " + e.getMessage());
        }
    }
}