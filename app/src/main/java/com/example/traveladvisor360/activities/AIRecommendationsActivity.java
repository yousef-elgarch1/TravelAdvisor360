package com.example.traveladvisor360.activities;



import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.traveladvisor360.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import java.util.ArrayList;
import java.util.List;

public class AIRecommendationsActivity extends AppCompatActivity {

    private MaterialAutoCompleteTextView budgetDropdown;
    private MaterialAutoCompleteTextView durationDropdown;
    private ChipGroup interestsChipGroup;
    private ChipGroup seasonChipGroup;
    private Button btnGenerate;
    private CircularProgressIndicator progressIndicator;
    private View resultsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activiy_ai_recommendation);

        initViews();
        setupDropdowns();
        setupChipGroups();
        setupGenerateButton();
    }

    private void initViews() {
        budgetDropdown = findViewById(R.id.budget_dropdown);
        durationDropdown = findViewById(R.id.duration_dropdown);
        interestsChipGroup = findViewById(R.id.interests_chip_group);
        seasonChipGroup = findViewById(R.id.season_chip_group);
        btnGenerate = findViewById(R.id.btn_generate);
        progressIndicator = findViewById(R.id.progress_indicator);
        resultsContainer = findViewById(R.id.results_container);
    }

    private void setupDropdowns() {
        // Budget dropdown
        String[] budgets = {"Budget", "Moderate", "Luxury", "Ultra Luxury"};
        ArrayAdapter<String> budgetAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, budgets);
        budgetDropdown.setAdapter(budgetAdapter);

        // Duration dropdown
        String[] durations = {"Weekend (2-3 days)", "Week (5-7 days)", "10 days", "2 weeks", "3 weeks", "1 month+"};
        ArrayAdapter<String> durationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, durations);
        durationDropdown.setAdapter(durationAdapter);
    }

    private void setupChipGroups() {
        // Interests
        String[] interests = {"Nature", "Culture", "History", "Food", "Adventure", "Beach", "Shopping", "Nightlife"};
        for (String interest : interests) {
            Chip chip = new Chip(this);
            chip.setText(interest);
            chip.setCheckable(true);
            interestsChipGroup.addView(chip);
        }

        // Season
        String[] seasons = {"Spring", "Summer", "Fall", "Winter"};
        for (String season : seasons) {
            Chip chip = new Chip(this);
            chip.setText(season);
            chip.setCheckable(true);
            seasonChipGroup.addView(chip);
        }
    }

    private void setupGenerateButton() {
        btnGenerate.setOnClickListener(v -> {
            generateRecommendations();
        });
    }

    private void generateRecommendations() {
        // Get selected values
        String budget = budgetDropdown.getText().toString();
        String duration = durationDropdown.getText().toString();

        List<String> selectedInterests = new ArrayList<>();
        for (int i = 0; i < interestsChipGroup.getChildCount(); i++) {
            Chip chip = (Chip) interestsChipGroup.getChildAt(i);
            if (chip.isChecked()) {
                selectedInterests.add(chip.getText().toString());
            }
        }

        List<String> selectedSeasons = new ArrayList<>();
        for (int i = 0; i < seasonChipGroup.getChildCount(); i++) {
            Chip chip = (Chip) seasonChipGroup.getChildAt(i);
            if (chip.isChecked()) {
                selectedSeasons.add(chip.getText().toString());
            }
        }

        showLoading(true);

        // Simulate API call
        new android.os.Handler().postDelayed(() -> {
            showLoading(false);
            showResults();
        }, 2000);
    }

    private void showLoading(boolean show) {
        progressIndicator.setVisibility(show ? View.VISIBLE : View.GONE);
        btnGenerate.setEnabled(!show);
        resultsContainer.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private void showResults() {
        // Show AI generated recommendations
        // This would typically populate a RecyclerView with recommended destinations
    }
}
