package com.example.traveladvisor360.activities;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.traveladvisor360.R;
import com.example.traveladvisor360.adapters.ItineraryAdapter;
import com.example.traveladvisor360.models.Itinerary;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class ItinerariesActivity extends AppCompatActivity {

    private RecyclerView rvItineraries;
    private ItineraryAdapter itineraryAdapter;
    private FloatingActionButton fabAdd;
    private View emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itineraries);

        initViews();
        setupRecyclerView();
        setupFab();
        loadItineraries();
    }

    private void initViews() {
        rvItineraries = findViewById(R.id.rv_itineraries);
        fabAdd = findViewById(R.id.fab_add);
        emptyView = findViewById(R.id.empty_view);
    }

    private void setupRecyclerView() {
        rvItineraries.setLayoutManager(new LinearLayoutManager(this));
        itineraryAdapter = new ItineraryAdapter(this);
        rvItineraries.setAdapter(itineraryAdapter);

        itineraryAdapter.setOnItineraryClickListener(itinerary -> {
            Intent intent = new Intent(this, ItineraryDetailsActivity.class);
            intent.putExtra("itinerary_id", itinerary.getId());
            startActivity(intent);
        });
    }

    private void setupFab() {
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(this, TripPlanningActivity.class);
            startActivity(intent);
        });
    }

    private void loadItineraries() {
        // Load from API or local database
        List<Itinerary> itineraries = new ArrayList<>();

        if (itineraries.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
            rvItineraries.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            rvItineraries.setVisibility(View.VISIBLE);
            itineraryAdapter.setItineraries(itineraries);
        }
    }
}
