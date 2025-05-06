package com.example.traveladvisor360.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.traveladvisor360.R;
import com.example.traveladvisor360.adapters.ReviewAdapter;
import com.example.traveladvisor360.models.Experience;
import com.example.traveladvisor360.utils.ApiClient;
import com.google.android.material.button.MaterialButton;

public class ExperienceDetailsFragment extends Fragment {

    private ImageView ivExperienceImage;
    private TextView tvExperienceName;
    private TextView tvExperienceDescription;
    private TextView tvExperienceLocation;
    private TextView tvExperiencePrice;
    private TextView tvExperienceRating;
    private TextView tvExperienceDuration;
    private RecyclerView rvReviews;
    private MaterialButton btnBookExperience;
    private View loadingView;
    private View rootView;
    private TextView tvEmptyState;

    private String experienceId;
    private Experience currentExperience;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_experience_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.rootView = view;

        if (getArguments() != null) {
            experienceId = getArguments().getString("experience_id");
        }

        initViews(view);
        setupClickListeners();
        loadExperienceDetails();
    }

    private void initViews(View view) {
        tvExperienceDescription = view.findViewById(R.id.tv_experience_description);
        tvExperienceLocation = view.findViewById(R.id.tv_experience_location);
        tvExperiencePrice = view.findViewById(R.id.tv_experience_price);
        tvExperienceRating = view.findViewById(R.id.tv_experience_rating);
        rvReviews = view.findViewById(R.id.rv_reviews);
        loadingView = view.findViewById(R.id.loading_view);
        tvEmptyState = view.findViewById(R.id.tv_empty_state);

        // Setup RecyclerView
        rvReviews.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvReviews.setNestedScrollingEnabled(false);
    }

    private void setupClickListeners() {
        btnBookExperience.setOnClickListener(v -> {
            // Navigate to booking screen
            Toast.makeText(requireContext(), "Booking functionality not implemented", Toast.LENGTH_SHORT).show();
        });
    }

    private void loadExperienceDetails() {
        if (experienceId == null || experienceId.isEmpty()) {
            showEmptyState("Experience not found");
            return;
        }

        showLoading(true);

        // In a real app, this would be an API call
        // For now, just use mock data
        // Simulating API delay
        rootView.postDelayed(() -> {
            // Create mock data
            currentExperience = new Experience();
            currentExperience.setId(experienceId);
            currentExperience.setDescription("Experience the breathtaking views of the countryside from a hot air balloon as the sun rises over the horizon. Our expert pilots will guide you through this unforgettable journey.");
            currentExperience.setLocation("Cappadocia, Turkey");
            currentExperience.setRating(4.8f);
            currentExperience.setImageUrl("https://example.com/balloon_ride.jpg");

            displayExperienceDetails(currentExperience);
            showLoading(false);
        }, 1000);
    }
    private void displayExperienceDetails(Experience experience) {
        tvExperienceDescription.setText(experience.getDescription());
        tvExperienceLocation.setText(experience.getLocation());
        tvExperienceRating.setText(String.valueOf(experience.getRating()) + "/5");

        // Load image
        if (experience.getImageUrl() != null && !experience.getImageUrl().isEmpty()) {
            Glide.with(this)
                    .load(experience.getImageUrl())
                    .placeholder(R.drawable.placeholder_experience)
                    .error(R.drawable.placeholder_experience)
                    .centerCrop()
                    .into(ivExperienceImage);
        }

        // Load reviews (In a real app, this would be another API call)
        // For now, just show a message
        tvEmptyState.setText("No reviews yet");
        tvEmptyState.setVisibility(View.VISIBLE);
    }

    private void showLoading(boolean show) {
        loadingView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void showEmptyState(String message) {
        tvEmptyState.setText(message);
        tvEmptyState.setVisibility(View.VISIBLE);
        showLoading(false);
    }
}