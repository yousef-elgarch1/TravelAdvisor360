package com.example.traveladvisor360.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.traveladvisor360.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.slider.RangeSlider;

import java.util.List;

public class FilterFragment extends Fragment {

    private ChipGroup continentChipGroup;
    private ChipGroup styleChipGroup;
    private RangeSlider priceRangeSlider;
    private Button btnApplyFilters;
    private Button btnResetFilters;
    private FilterListener filterListener;

    public interface FilterListener {
        void onFiltersApplied(String continent, String style, float minPrice, float maxPrice);
    }

    public void setFilterListener(FilterListener listener) {
        this.filterListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filter, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        setupFilters();
        setupClickListeners();
    }

    private void initViews(View view) {
        continentChipGroup = view.findViewById(R.id.continent_chip_group);
        styleChipGroup = view.findViewById(R.id.style_chip_group);
        priceRangeSlider = view.findViewById(R.id.price_range_slider);
        btnApplyFilters = view.findViewById(R.id.btn_apply_filters);
        btnResetFilters = view.findViewById(R.id.btn_reset_filters);
    }

    private void setupFilters() {
        String[] continents = {"All", "Europe", "Asia", "Africa", "North America", "South America", "Oceania"};
        for (String continent : continents) {
            Chip chip = new Chip(requireContext());
            chip.setText(continent);
            chip.setCheckable(true);
            continentChipGroup.addView(chip);
        }

        String[] styles = {"All", "Adventure", "Cultural", "Relaxation", "Romantic", "Family"};
        for (String style : styles) {
            Chip chip = new Chip(requireContext());
            chip.setText(style);
            chip.setCheckable(true);
            styleChipGroup.addView(chip);
        }
    }

    private void setupClickListeners() {
        btnApplyFilters.setOnClickListener(v -> applyFilters());
        btnResetFilters.setOnClickListener(v -> resetFilters());
    }

    private void applyFilters() {
        String selectedContinent = "";
        String selectedStyle = "";

        int continentChipId = continentChipGroup.getCheckedChipId();
        if (continentChipId != View.NO_ID) {
            Chip chip = continentChipGroup.findViewById(continentChipId);
            selectedContinent = chip.getText().toString();
        }

        int styleChipId = styleChipGroup.getCheckedChipId();
        if (styleChipId != View.NO_ID) {
            Chip chip = styleChipGroup.findViewById(styleChipId);
            selectedStyle = chip.getText().toString();
        }

        List<Float> priceRange = priceRangeSlider.getValues();
        float minPrice = priceRange.get(0);
        float maxPrice = priceRange.get(1);

        if (filterListener != null) {
            filterListener.onFiltersApplied(selectedContinent, selectedStyle, minPrice, maxPrice);
        }
    }

    private void resetFilters() {
        continentChipGroup.clearCheck();
        styleChipGroup.clearCheck();
        priceRangeSlider.setValues(0f, 1000f);
    }
}
