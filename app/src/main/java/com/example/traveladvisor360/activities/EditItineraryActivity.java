package com.example.traveladvisor360.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.traveladvisor360.R;
import com.example.traveladvisor360.models.Itinerary;
import com.example.traveladvisor360.models.ItineraryDay;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EditItineraryActivity extends AppCompatActivity {

    private String itineraryId;
    private Itinerary itinerary;
    private boolean isNewItinerary = false;

    private ImageView ivCover;
    private TextInputLayout titleLayout;
    private TextInputEditText etTitle;
    private TextInputLayout destinationLayout;
    private TextInputEditText etDestination;
    private TextInputLayout startDateLayout;
    private TextInputEditText etStartDate;
    private TextInputLayout endDateLayout;
    private TextInputEditText etEndDate;
    private TextInputLayout budgetLayout;
    private TextInputEditText etBudget;
    private TextInputLayout notesLayout;
    private TextInputEditText etNotes;
    private RecyclerView rvDays;
    private MaterialButton btnAddDay;
    private FloatingActionButton fabSave;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());
    private Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_itinerary);

        // Get itinerary ID from intent
        itineraryId = getIntent().getStringExtra("itinerary_id");
        isNewItinerary = itineraryId == null;

        initViews();
        setupToolbar();
        setupDatePickers();
        setupButtons();
        loadItinerary();
    }

    private void initViews() {
        ivCover = findViewById(R.id.iv_cover);
        titleLayout = findViewById(R.id.title_layout);
        etTitle = findViewById(R.id.et_title);
        destinationLayout = findViewById(R.id.destination_layout);
        etDestination = findViewById(R.id.et_destination);
        startDateLayout = findViewById(R.id.start_date_layout);
        etStartDate = findViewById(R.id.et_start_date);
        endDateLayout = findViewById(R.id.end_date_layout);
        etEndDate = findViewById(R.id.et_end_date);
        budgetLayout = findViewById(R.id.budget_layout);
        etBudget = findViewById(R.id.et_budget);
        notesLayout = findViewById(R.id.notes_layout);
        etNotes = findViewById(R.id.et_notes);
        rvDays = findViewById(R.id.rv_days);
        btnAddDay = findViewById(R.id.btn_add_day);
        fabSave = findViewById(R.id.fab_save);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(isNewItinerary ? "Create Itinerary" : "Edit Itinerary");
        }
    }

    private void setupDatePickers() {
        etStartDate.setOnClickListener(v -> showDatePicker(etStartDate, calendar));
        etEndDate.setOnClickListener(v -> showDatePicker(etEndDate, calendar));
    }

    private void showDatePicker(EditText editText, Calendar calendar) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    Calendar selectedCalendar = Calendar.getInstance();
                    selectedCalendar.set(year, month, dayOfMonth);
                    editText.setText(dateFormat.format(selectedCalendar.getTime()));
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void setupButtons() {
        btnAddDay.setOnClickListener(v -> addNewDay());
        fabSave.setOnClickListener(v -> saveItinerary());
    }

    private void loadItinerary() {
        if (isNewItinerary) {
            // Create a new itinerary
            itinerary = new Itinerary();
            itinerary.setStartDate(new Date());

            Calendar endCalendar = Calendar.getInstance();
            endCalendar.add(Calendar.DAY_OF_MONTH, 6); // Default to a week-long trip
            itinerary.setEndDate(endCalendar.getTime());

            // Show today's date and a week later in the date fields
            etStartDate.setText(dateFormat.format(itinerary.getStartDate()));
            etEndDate.setText(dateFormat.format(itinerary.getEndDate()));

            // Generate initial day (day 1)
            List<ItineraryDay> days = new ArrayList<>();
            ItineraryDay day1 = new ItineraryDay();
            day1.setId("day1");
            day1.setDayNumber(1);
            day1.setDate(itinerary.getStartDate());
            days.add(day1);
            itinerary.setDays(days);

            updateUI();
        } else {
            // In a real app, you would load the itinerary from your database or API
            // For this example, we'll use a mock itinerary
            loadMockItinerary();
        }
    }

    private void loadMockItinerary() {
        // This would be replaced with actual data retrieval in a real app
        itinerary = createMockItinerary();
        updateUI();
    }

    private void updateUI() {
        if (itinerary == null) {
            return;
        }

        // Load cover image if available
        if (itinerary.getCoverImage() != null && !itinerary.getCoverImage().isEmpty()) {
            Glide.with(this)
                    .load(getImageResource(itinerary.getCoverImage()))
                    .centerCrop()
                    .into(ivCover);
        }

        // Fill form fields
        etTitle.setText(itinerary.getTitle());
        etDestination.setText(itinerary.getDestination());
        etStartDate.setText(dateFormat.format(itinerary.getStartDate()));
        etEndDate.setText(dateFormat.format(itinerary.getEndDate()));
        etBudget.setText(String.valueOf(itinerary.getBudget()));
        etNotes.setText(itinerary.getNotes());
    }

    private int getImageResource(String imageName) {
        if (imageName.startsWith("http")) {
            return 0; // Will be loaded via URL
        }
        int resourceId = getResources().getIdentifier(
                imageName, "drawable", getPackageName());
        return resourceId != 0 ? resourceId : R.drawable.placeholder_image;
    }

    private Itinerary createMockItinerary() {
        // Create a mock itinerary with the given ID
        Itinerary mockItinerary = new Itinerary();
        mockItinerary.setId(itineraryId);
        mockItinerary.setTitle("Rome Adventure");
        mockItinerary.setDestination("Rome, Italy");

        // Set dates
        Calendar calendar = Calendar.getInstance();
        mockItinerary.setStartDate(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, 6);
        mockItinerary.setEndDate(calendar.getTime());

        mockItinerary.setCoverImage("placeholder_image");
        mockItinerary.setBudget(2500);
        mockItinerary.setNotes("Remember to pack comfortable shoes for walking tours and formal attire for nice restaurants!");

        // Create some itinerary days
        List<ItineraryDay> days = new ArrayList<>();

        // Day 1
        ItineraryDay day1 = new ItineraryDay();
        day1.setId("day1");
        day1.setDayNumber(1);
        calendar = Calendar.getInstance();
        day1.setDate(calendar.getTime());
        day1.setNotes("Arrival day - take it easy and adjust to the time zone");
        days.add(day1);

        // Set days to itinerary
        mockItinerary.setDays(days);

        return mockItinerary;
    }

    private void addNewDay() {
        if (itinerary.getDays() == null) {
            itinerary.setDays(new ArrayList<>());
        }

        int nextDayNumber = itinerary.getDays().size() + 1;

        // Calculate the date for the new day
        Calendar calendar = Calendar.getInstance();
        if (!itinerary.getDays().isEmpty()) {
            // Use the last day's date and add one day
            ItineraryDay lastDay = itinerary.getDays().get(itinerary.getDays().size() - 1);
            calendar.setTime(lastDay.getDate());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        } else {
            // Use the start date from the itinerary
            calendar.setTime(itinerary.getStartDate());
        }

        ItineraryDay newDay = new ItineraryDay();
        newDay.setId("day" + nextDayNumber);
        newDay.setDayNumber(nextDayNumber);
        newDay.setDate(calendar.getTime());

        itinerary.getDays().add(newDay);

        // Update list
        Toast.makeText(this, "Day " + nextDayNumber + " added", Toast.LENGTH_SHORT).show();
    }

    private boolean validateForm() {
        boolean isValid = true;

        if (etTitle.getText().toString().trim().isEmpty()) {
            titleLayout.setError("Title is required");
            isValid = false;
        } else {
            titleLayout.setError(null);
        }

        if (etDestination.getText().toString().trim().isEmpty()) {
            destinationLayout.setError("Destination is required");
            isValid = false;
        } else {
            destinationLayout.setError(null);
        }

        if (etStartDate.getText().toString().trim().isEmpty()) {
            startDateLayout.setError("Start date is required");
            isValid = false;
        } else {
            startDateLayout.setError(null);
        }

        if (etEndDate.getText().toString().trim().isEmpty()) {
            endDateLayout.setError("End date is required");
            isValid = false;
        } else {
            endDateLayout.setError(null);
        }

        // Validate that end date is after start date
        try {
            Date startDate = dateFormat.parse(etStartDate.getText().toString());
            Date endDate = dateFormat.parse(etEndDate.getText().toString());

            if (endDate.before(startDate)) {
                endDateLayout.setError("End date must be after start date");
                isValid = false;
            } else {
                endDateLayout.setError(null);
            }
        } catch (Exception e) {
            // Date parsing exception, already handled by individual field validation
        }

        // Budget can be empty, but if provided, must be a valid number
        String budgetStr = etBudget.getText().toString().trim();
        if (!budgetStr.isEmpty()) {
            try {
                double budget = Double.parseDouble(budgetStr);
                if (budget < 0) {
                    budgetLayout.setError("Budget cannot be negative");
                    isValid = false;
                } else {
                    budgetLayout.setError(null);
                }
            } catch (NumberFormatException e) {
                budgetLayout.setError("Enter a valid number");
                isValid = false;
            }
        } else {
            budgetLayout.setError(null);
        }

        return isValid;
    }

    private void saveItinerary() {
        if (!validateForm()) {
            return;
        }

        // Update itinerary object with form values
        itinerary.setTitle(etTitle.getText().toString().trim());
        itinerary.setDestination(etDestination.getText().toString().trim());

        try {
            itinerary.setStartDate(dateFormat.parse(etStartDate.getText().toString()));
            itinerary.setEndDate(dateFormat.parse(etEndDate.getText().toString()));
        } catch (Exception e) {
            // Shouldn't happen as we've validated the dates
            e.printStackTrace();
        }

        String budgetStr = etBudget.getText().toString().trim();
        if (!budgetStr.isEmpty()) {
            itinerary.setBudget(Double.parseDouble(budgetStr));
        }

        itinerary.setNotes(etNotes.getText().toString().trim());

        // In a real app, you would save the itinerary to your database or API
        // For this example, we'll just show a success message and finish the activity

        Toast.makeText(this, isNewItinerary ? "Itinerary created" : "Itinerary updated", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_itinerary, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            confirmExit();
            return true;
        } else if (id == R.id.action_discard) {
            confirmExit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void confirmExit() {
        new AlertDialog.Builder(this)
                .setTitle("Discard Changes")
                .setMessage("Are you sure you want to discard your changes?")
                .setPositiveButton("Discard", (dialog, which) -> finish())
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onBackPressed() {
        confirmExit();
    }
}