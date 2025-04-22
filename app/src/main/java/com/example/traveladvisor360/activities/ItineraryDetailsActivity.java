package com.example.traveladvisor360.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.traveladvisor360.R;
import com.example.traveladvisor360.adapters.ItineraryDayAdapter;
import com.example.traveladvisor360.models.Itinerary;
import com.example.traveladvisor360.models.ItineraryDay;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ItineraryDetailsActivity extends AppCompatActivity {

    private String itineraryId;
    private Itinerary itinerary;

    private CollapsingToolbarLayout collapsingToolbar;
    private ImageView ivCover;
    private TextView tvDestination;
    private TextView tvDates;
    private TextView tvDuration;
    private TextView tvNotes;
    private RecyclerView rvDays;
    private ItineraryDayAdapter dayAdapter;
    private FloatingActionButton fabEdit;
    private View emptyStateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itnerary_details);

        // Get itinerary ID from intent
        itineraryId = getIntent().getStringExtra("itinerary_id");
        if (itineraryId == null) {
            finish();
            return;
        }

        initViews();
        setupToolbar();
        setupRecyclerView();
        setupFab();
        loadItinerary();
    }

    private void initViews() {
        collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        ivCover = findViewById(R.id.iv_cover);
        tvDestination = findViewById(R.id.tv_destination);
        tvDates = findViewById(R.id.tv_dates);
        tvDuration = findViewById(R.id.tv_duration);
        tvNotes = findViewById(R.id.tv_notes);
        rvDays = findViewById(R.id.rv_days);
        fabEdit = findViewById(R.id.fab_edit);
        emptyStateView = findViewById(R.id.empty_state);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void setupRecyclerView() {
        rvDays.setLayoutManager(new LinearLayoutManager(this));
        dayAdapter = new ItineraryDayAdapter(this);
        rvDays.setAdapter(dayAdapter);
    }

    private void setupFab() {
        fabEdit.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditItineraryActivity.class);
            intent.putExtra("itinerary_id", itineraryId);
            startActivity(intent);
        });
    }

    private void loadItinerary() {
        // In a real app, you would load the itinerary from your database or API
        // For this example, we'll create a mock itinerary

        // Mock data
        itinerary = createMockItinerary();

        // Update UI with itinerary data
        updateUI();
    }

    private void updateUI() {
        if (itinerary == null) {
            return;
        }

        // Set title in the collapsing toolbar
        collapsingToolbar.setTitle(itinerary.getTitle());

        // Load cover image
        if (itinerary.getCoverImageUrl() != null && !itinerary.getCoverImageUrl().isEmpty()) {
            Glide.with(this)
                    .load(getImageResource(itinerary.getCoverImageUrl()))
                    .centerCrop()
                    .into(ivCover);
        }

        // Set itinerary details
        tvDestination.setText(itinerary.getDestination());
        tvDates.setText(itinerary.getFormattedDateRange());
        tvDuration.setText(itinerary.getDuration() + " days");

        // Set notes if available
        if (itinerary.getNotes() != null && !itinerary.getNotes().isEmpty()) {
            tvNotes.setText(itinerary.getNotes());
            tvNotes.setVisibility(View.VISIBLE);
        } else {
            tvNotes.setVisibility(View.GONE);
        }

        // Update days list
        List<ItineraryDay> days = itinerary.getDays();
        if (days.isEmpty()) {
            rvDays.setVisibility(View.GONE);
            emptyStateView.setVisibility(View.VISIBLE);
        } else {
            rvDays.setVisibility(View.VISIBLE);
            emptyStateView.setVisibility(View.GONE);
            dayAdapter.setDays(days);
        }
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
        // Create a mock itinerary for demonstration
        Itinerary mockItinerary = new Itinerary();
        mockItinerary.setId(itineraryId);
        mockItinerary.setTitle("Rome Adventure");
        mockItinerary.setDestination("Rome, Italy");

        // Set dates (current date and a week later)
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        mockItinerary.setStartDate(calendar.getTime());
        calendar.add(java.util.Calendar.DAY_OF_MONTH, 6);
        mockItinerary.setEndDate(calendar.getTime());

        mockItinerary.setCoverImageUrl("placeholder_image"); // Use a drawable resource
        mockItinerary.setNotes("Remember to pack comfortable shoes for walking tours and formal attire for nice restaurants!");

        // Create some itinerary days with activities
        List<ItineraryDay> days = new ArrayList<>();

        // Day 1
        ItineraryDay day1 = new ItineraryDay();
        day1.setId("day1");
        day1.setDayNumber(1);
        calendar = java.util.Calendar.getInstance();
        day1.setDate(calendar.getTime());
        day1.setNotes("Arrival day - take it easy and adjust to the time zone");

        // Add some activities to day 1
        days.add(day1);

        // Set days to itinerary
        mockItinerary.setDays(days);

        return mockItinerary;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_itinerary_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (id == R.id.action_share) {
            shareItinerary();
            return true;
        } else if (id == R.id.action_delete) {
            deleteItinerary();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void shareItinerary() {
        // Implement sharing functionality
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, itinerary.getTitle());
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                "Check out my trip to " + itinerary.getDestination() +
                        " from " + itinerary.getFormattedDateRange() + "!");
        startActivity(Intent.createChooser(shareIntent, "Share Itinerary"));
    }

    private void deleteItinerary() {
        // Implement delete functionality
        // Show confirmation dialog before deleting

        // For this example, just finish the activity
        finish();
    }
}