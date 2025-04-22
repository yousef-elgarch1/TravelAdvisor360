package com.example.traveladvisor360.activities;


import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.traveladvisor360.R;
import com.example.traveladvisor360.adapters.DestinationAdapter;
import com.example.traveladvisor360.models.Destination;
import com.example.traveladvisor360.utils.ApiCallback;
import com.example.traveladvisor360.utils.ApiClient;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import java.util.List;

public class DestinationsActivity extends AppCompatActivity {

    private RecyclerView rvDestinations;
    private DestinationAdapter destinationAdapter;
    private ChipGroup continentChipGroup;
    private ChipGroup styleChipGroup;
    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destinations);

        initViews();
        setupToolbar();
        setupRecyclerView();
        setupFilters();
        loadDestinations("");
    }

    private void initViews() {
        rvDestinations = findViewById(R.id.rv_destinations);
        continentChipGroup = findViewById(R.id.continent_chip_group);
        styleChipGroup = findViewById(R.id.style_chip_group);
        toolbar = findViewById(R.id.toolbar);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Destinations");
        }
    }

    private void setupRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        rvDestinations.setLayoutManager(layoutManager);
        destinationAdapter = new DestinationAdapter(this);
        rvDestinations.setAdapter(destinationAdapter);
    }

    private void setupFilters() {
        // Continent Filter
        String[] continents = {"All", "Europe", "Asia", "Africa", "North America", "South America", "Oceania"};
        for (String continent : continents) {
            Chip chip = new Chip(this);
            chip.setText(continent);
            chip.setCheckable(true);
            continentChipGroup.addView(chip);
        }

        // Travel Style Filter
        String[] styles = {"All", "Adventure", "Cultural", "Relaxation", "Romantic", "Family"};
        for (String style : styles) {
            Chip chip = new Chip(this);
            chip.setText(style);
            chip.setCheckable(true);
            styleChipGroup.addView(chip);
        }

        // Set listeners
        continentChipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            Chip chip = findViewById(checkedId);
            if (chip != null) {
                String filter = chip.getText().toString();
                loadDestinations(filter);
            }
        });

        styleChipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            Chip chip = findViewById(checkedId);
            if (chip != null) {
                String filter = chip.getText().toString();
                loadDestinations(filter);
            }
        });
    }

    private void loadDestinations(String filter) {
        ApiClient.getInstance().getDestinations(new ApiCallback<List<Destination>>() {
            @Override
            public void onSuccess(List<Destination> result) {
                runOnUiThread(() -> {
                    destinationAdapter.setDestinations(result);
                });
            }

            @Override
            public void onError(String error) {
                // Handle error
            }
        });
    }
}