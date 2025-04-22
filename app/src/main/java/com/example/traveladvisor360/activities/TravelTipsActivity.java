package com.example.traveladvisor360.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.traveladvisor360.R;
import com.example.traveladvisor360.adapters.TravelTipsAdapter;
import com.example.traveladvisor360.models.TravelTip;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import java.util.ArrayList;
import java.util.List;

public class TravelTipsActivity extends AppCompatActivity {

    private RecyclerView rvTravelTips;
    private TravelTipsAdapter travelTipsAdapter;
    private ChipGroup categoryChipGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_tips);

        initViews();
        setupRecyclerView();
        setupCategoryFilter();
        loadTravelTips("");
    }

    private void initViews() {
        rvTravelTips = findViewById(R.id.rv_travel_tips);
        categoryChipGroup = findViewById(R.id.category_chip_group);
    }

    private void setupRecyclerView() {
        rvTravelTips.setLayoutManager(new LinearLayoutManager(this));
        travelTipsAdapter = new TravelTipsAdapter(this);
        rvTravelTips.setAdapter(travelTipsAdapter);
    }

    private void setupCategoryFilter() {
        String[] categories = {"All", "Packing", "Safety", "Money", "Culture", "Photography"};
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
                loadTravelTips(category);
            }
        });
    }

    private void loadTravelTips(String category) {
        // Load travel tips based on category
        List<TravelTip> tips = createMockTravelTips();
        travelTipsAdapter.setTravelTips(tips);
    }

    private List<TravelTip> createMockTravelTips() {
        List<TravelTip> tips = new ArrayList<>();

        TravelTip tip1 = new TravelTip();
        tip1.setId("1");
        tip1.setTitle("Pack Light, Travel Far");
        tip1.setCategory("Packing");
        tip1.setDescription("Learn the art of minimalist packing for stress-free travel adventures.");
        tip1.setImageUrl("packing_tip_image");
        tips.add(tip1);

        TravelTip tip2 = new TravelTip();
        tip2.setId("2");
        tip2.setTitle("Stay Safe While Traveling");
        tip2.setCategory("Safety");
        tip2.setDescription("Essential safety tips for protecting yourself and your belongings abroad.");
        tip2.setImageUrl("safety_tip_image");
        tips.add(tip2);

        TravelTip tip3 = new TravelTip();
        tip3.setId("3");
        tip3.setTitle("Money-Saving Travel Hacks");
        tip3.setCategory("Money");
        tip3.setDescription("Smart strategies to make your travel budget go further.");
        tip3.setImageUrl("money_tip_image");
        tips.add(tip3);

        return tips;
    }
}

