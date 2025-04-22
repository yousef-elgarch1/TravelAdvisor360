package com.example.traveladvisor360.activities;


import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.traveladvisor360.R;
import com.example.traveladvisor360.adapters.ExperienceAdapter;
import com.example.traveladvisor360.models.Experience;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import java.util.ArrayList;
import java.util.List;

public class ExperiencesActivity extends AppCompatActivity {

    private RecyclerView rvExperiences;
    private ExperienceAdapter experienceAdapter;
    private ChipGroup categoryChipGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experiences);

        initViews();
        setupRecyclerView();
        setupCategoryFilter();
        loadExperiences("");
    }

    private void initViews() {
        rvExperiences = findViewById(R.id.rv_experiences);
        categoryChipGroup = findViewById(R.id.category_chip_group);
    }

    private void setupRecyclerView() {
        rvExperiences.setLayoutManager(new LinearLayoutManager(this));
        experienceAdapter = new ExperienceAdapter(this);
        rvExperiences.setAdapter(experienceAdapter);
    }

    private void setupCategoryFilter() {
        String[] categories = {"All", "Nature", "Food & Drink", "Adventure", "Cultural", "Wellness"};
        for (String category : categories) {
            Chip chip = new Chip(this);
            chip.setText(category);
            chip.setCheckable(true);
            categoryChipGroup.addView(chip);
        }

        categoryChipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            Chip chip = findViewById(checkedId);
            if (chip != null) {
                String category = chip.getText().toString();
                loadExperiences(category);
            }
        });
    }

    private void loadExperiences(String category) {
        // Load experiences based on category
        List<Experience> experiences = new ArrayList<>();
        experienceAdapter.setExperiences(experiences);
    }
}
