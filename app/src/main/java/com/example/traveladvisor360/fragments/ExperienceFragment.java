package com.example.traveladvisor360.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.traveladvisor360.R;
import com.example.traveladvisor360.adapters.ExperienceAdapter;
import com.example.traveladvisor360.models.Experience;
import com.example.traveladvisor360.utils.ApiCallback;
import com.example.traveladvisor360.utils.ApiClient;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

public class ExperienceFragment extends Fragment {

    private RecyclerView rvExperiences;
    private ExperienceAdapter experienceAdapter;
    private ChipGroup categoryChipGroup;
    private TextView tvTitle;
    private TextView tvSubtitle;
    private View loadingView;
    private TextView tvEmptyState;

    private String currentCategory = "All";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_experiences, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        setupRecyclerView();
        setupCategoryFilter();
        loadExperiences(currentCategory);
    }

    private void initViews(View view) {
        rvExperiences = view.findViewById(R.id.rv_experiences);
        categoryChipGroup = view.findViewById(R.id.category_chip_group);
        tvTitle = view.findViewById(R.id.tv_title);
        tvSubtitle = view.findViewById(R.id.tv_subtitle);
        loadingView = view.findViewById(R.id.loading_view);
        tvEmptyState = view.findViewById(R.id.tv_empty_state);
    }

    private void setupRecyclerView() {
        experienceAdapter = new ExperienceAdapter(requireContext());
        rvExperiences.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        rvExperiences.setAdapter(experienceAdapter);

        experienceAdapter.setOnExperienceClickListener(experience -> {
            // Navigate to experience details
            Bundle args = new Bundle();
            args.putString("experience_id", experience.getId());
            // Navigate using NavController
        });
    }

    private void setupCategoryFilter() {
        String[] categories = {"All", "Nature", "Food & Drink", "Adventure", "Cultural", "Wellness"};

        for (String category : categories) {
            Chip chip = new Chip(requireContext());
            chip.setText(category);
            chip.setCheckable(true);
            chip.setChecked(category.equals(currentCategory));
            categoryChipGroup.addView(chip);

            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    currentCategory = category;
                    updateTitle();
                    loadExperiences(category);

                    // Uncheck other chips
                    for (int i = 0; i < categoryChipGroup.getChildCount(); i++) {
                        Chip otherChip = (Chip) categoryChipGroup.getChildAt(i);
                        if (otherChip != chip) {
                            otherChip.setChecked(false);
                        }
                    }
                }
            });
        }
    }

    private void updateTitle() {
        if (currentCategory.equals("All")) {
            tvTitle.setText(R.string.unique_experiences);
            tvSubtitle.setText(R.string.immerse_yourself);
        } else {
            tvTitle.setText(currentCategory + " Experiences");
            tvSubtitle.setText("Discover unforgettable " + currentCategory.toLowerCase() + " experiences worldwide");
        }
    }

    private void loadExperiences(String category) {
        showLoading(true);

        if (category.equals("All")) {
            ApiClient.getInstance().getExperiences(new ApiCallback<List<Experience>>() {
                @Override
                public void onSuccess(List<Experience> result) {
                    showLoading(false);

                    if (result.isEmpty()) {
                        showEmptyState(true);
                    } else {
                        showEmptyState(false);
                        experienceAdapter.setExperiences(result);
                    }
                }

                @Override
                public void onError(String error) {
                    showLoading(false);

                    // For demo, load mock data on error
                    List<Experience> mockExperiences = createMockExperiences();
                    if (mockExperiences.isEmpty()) {
                        showEmptyState(true);
                    } else {
                        showEmptyState(false);
                        experienceAdapter.setExperiences(mockExperiences);
                    }
                }
            });
        } else {
            // Filter by category
            // In a real app, this would make an API call with category filter
            List<Experience> mockExperiences = createMockExperiences();
            List<Experience> filteredExperiences = new ArrayList<>();

            for (Experience experience : mockExperiences) {
                if (experience.getCategory().equals(category)) {
                    filteredExperiences.add(experience);
                }
            }

            showLoading(false);

            if (filteredExperiences.isEmpty()) {
                showEmptyState(true);
            } else {
                showEmptyState(false);
                experienceAdapter.setExperiences(filteredExperiences);
            }
        }
    }

    private void showLoading(boolean show) {
        loadingView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void showEmptyState(boolean show) {
        tvEmptyState.setVisibility(show ? View.VISIBLE : View.GONE);
        rvExperiences.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private List<Experience> createMockExperiences() {
        List<Experience> experiences = new ArrayList<>();

        // Northern Lights Experience
        Experience northernLights = new Experience();
        northernLights.setId("1");
        northernLights.setTitle("Northern Lights Safari");
        northernLights.setLocation("Tromsø, Norway");
        northernLights.setDescription("Chase the magical Aurora Borealis with expert guides in the Arctic wilderness.");
        northernLights.setImageUrl("northern_lights_image");
        northernLights.setCategory("Nature");
        northernLights.setRating(4.9f);
        northernLights.setReviewCount(45);
        northernLights.setPrice(129.0);
        northernLights.setDuration(4.0f);
        experiences.add(northernLights);

        // Cooking Class Experience
        Experience cookingClass = new Experience();
        cookingClass.setId("2");
        cookingClass.setTitle("Traditional Cooking Class");
        cookingClass.setLocation("Marrakech, Morocco");
        cookingClass.setDescription("Learn to prepare authentic Moroccan dishes with local chefs in a traditional riad.");
        cookingClass.setImageUrl("cooking_class_image");
        cookingClass.setCategory("Food & Drink");
        cookingClass.setRating(4.8f);
        cookingClass.setReviewCount(34);
        cookingClass.setPrice(65.0);
        cookingClass.setDuration(3.0f);
        experiences.add(cookingClass);

        // Hot Air Balloon Experience
        Experience balloonRide = new Experience();
        balloonRide.setId("3");
        balloonRide.setTitle("Sunrise Hot Air Balloon Ride");
        balloonRide.setLocation("Cappadocia, Turkey");
        balloonRide.setDescription("Soar above the fairy chimneys and unique landscapes at dawn for breathtaking views.");
        balloonRide.setImageUrl("balloon_ride_image");
        balloonRide.setCategory("Adventure");
        balloonRide.setRating(4.9f);
        balloonRide.setReviewCount(56);
        balloonRide.setPrice(175.0);
        balloonRide.setDuration(3.5f);
        experiences.add(balloonRide);

        // Temple Tour Experience
        Experience templeTour = new Experience();
        templeTour.setId("4");
        templeTour.setTitle("Ancient Temples Bike Tour");
        templeTour.setLocation("Siem Reap, Cambodia");
        templeTour.setDescription("Explore the magnificent temples of Angkor on a guided bicycle tour through the jungle.");
        templeTour.setImageUrl("temple_tour_image");
        templeTour.setCategory("Cultural");
        templeTour.setRating(4.7f);
        templeTour.setReviewCount(28);
        templeTour.setPrice(45.0);
        templeTour.setDuration(6.0f);
        experiences.add(templeTour);

        // Spa Experience
        Experience spaRetreat = new Experience();
        spaRetreat.setId("5");
        spaRetreat.setTitle("Balinese Spa Retreat");
        spaRetreat.setLocation("Ubud, Bali");
        spaRetreat.setDescription("Indulge in traditional Indonesian spa treatments in a serene jungle setting.");
        spaRetreat.setImageUrl("spa_retreat_image");
        spaRetreat.setCategory("Wellness");
        spaRetreat.setRating(4.8f);
        spaRetreat.setReviewCount(42);
        spaRetreat.setPrice(85.0);
        spaRetreat.setDuration(2.0f);
        experiences.add(spaRetreat);

        return experiences;
    }
}
