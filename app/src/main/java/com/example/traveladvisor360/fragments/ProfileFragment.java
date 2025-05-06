package com.example.traveladvisor360.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.traveladvisor360.activities.AuthActivity;
import com.example.traveladvisor360.adapters.ItineraryAdapter;
import com.example.traveladvisor360.callbacks.AuthCallback;
import com.example.traveladvisor360.models.Itinerary;
import com.example.traveladvisor360.models.User;
import com.example.traveladvisor360.network.ApiResponse;
import com.example.traveladvisor360.services.AuthService;
import com.example.traveladvisor360.utils.ApiClient;
import com.example.traveladvisor360.utils.SharedPreferencesManager;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.List;

public class ProfileFragment extends Fragment {

    private ImageView ivProfilePic;
    private TextView tvName;
    private TextView tvEmail;
    private Button btnEditProfile;
    private Button btnLogout;
    private RecyclerView rvUpcomingTrips;
    private ItineraryAdapter upcomingTripsAdapter;
    private MaterialCardView cardPreferences;
    private ChipGroup chipGroupInterests;
    private SwitchMaterial switchNotifications;
    private SwitchMaterial switchDarkMode;

    private AuthService authService;
    private SharedPreferencesManager preferencesManager;
    private User currentUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        authService = AuthService.getInstance(requireContext());
        preferencesManager = SharedPreferencesManager.getInstance(requireContext());

        initViews(view);
        setupRecyclerView();
        setupClickListeners();
        loadUserProfile();
    }

    private void initViews(View view) {
        ivProfilePic = view.findViewById(R.id.iv_profile_pic);
        tvName = view.findViewById(R.id.tv_name);
        tvEmail = view.findViewById(R.id.tv_email);
        btnEditProfile = view.findViewById(R.id.btn_edit_profile);
        btnLogout = view.findViewById(R.id.btn_logout);
        rvUpcomingTrips = view.findViewById(R.id.rv_upcoming_trips);
        cardPreferences = view.findViewById(R.id.card_preferences);
        chipGroupInterests = view.findViewById(R.id.chip_group_interests);
        switchNotifications = view.findViewById(R.id.switch_notifications);
        switchDarkMode = view.findViewById(R.id.switch_dark_mode);
    }

    private void setupRecyclerView() {
        upcomingTripsAdapter = new ItineraryAdapter(requireContext());
        rvUpcomingTrips.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvUpcomingTrips.setAdapter(upcomingTripsAdapter);
    }

    private void setupClickListeners() {
        btnEditProfile.setOnClickListener(v -> {
            // Navigate to edit profile screen or show dialog
            showEditProfileDialog();
        });

        btnLogout.setOnClickListener(v -> {
            showLogoutConfirmationDialog();
        });

        switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (currentUser != null && currentUser.getPreferences() != null) {
                currentUser.getPreferences().setReceiveNotifications(isChecked);
                // Save preference to server
            }
        });

        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferencesManager.setDarkMode(isChecked);
            // Apply theme change
            // This would typically restart the activity to apply the theme
        });
    }

    private void loadUserProfile() {
        // Get user from shared preferences or api
        currentUser = preferencesManager.getUser();

        if (currentUser != null) {
            displayUserProfile(currentUser);
        } else {
            // Call API to get current user
            ApiClient.getInstance().getApiService().getCurrentUser().enqueue(new retrofit2.Callback<ApiResponse<User>>() {
                @Override
                public void onResponse(retrofit2.Call<ApiResponse<User>> call, retrofit2.Response<ApiResponse<User>> response) {
                    if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                        currentUser = response.body().getData();
                        preferencesManager.saveUser(currentUser);
                        displayUserProfile(currentUser);
                    } else {
                        // Handle error
                        Toast.makeText(requireContext(), "Failed to load profile", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<ApiResponse<User>> call, Throwable t) {
                    // Handle error
                    Toast.makeText(requireContext(), "Network error", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Load upcoming trips
        // In a real app, this would make an API call
        // For now, just use mock data if available
    }

    private void displayUserProfile(User user) {
        tvName.setText(user.getName());
        tvEmail.setText(user.getEmail());

        // Load profile picture
        if (user.getProfilePicUrl() != null && !user.getProfilePicUrl().isEmpty()) {
            Glide.with(this)
                    .load(user.getProfilePicUrl())
                    .circleCrop()
                    .placeholder(R.drawable.ic_profile)
                    .into(ivProfilePic);
        }

        // Set up interests
        if (user.getPreferences() != null) {
            List<String> interests = user.getPreferences().getTravelInterests();
            for (String interest : interests) {
                Chip chip = new Chip(requireContext());
                chip.setText(interest);
                chip.setCheckable(false);
                chipGroupInterests.addView(chip);
            }

            // Set notification preference
            switchNotifications.setChecked(user.getPreferences().isReceiveNotifications());
        }

        // Set dark mode switch
        switchDarkMode.setChecked(preferencesManager.isDarkMode());
    }

    private void showEditProfileDialog() {
        // Show dialog to edit profile
        // This is just a simple example
        Toast.makeText(requireContext(), "Edit profile functionality not implemented", Toast.LENGTH_SHORT).show();
    }

    private void showLogoutConfirmationDialog() {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    performLogout();
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void performLogout() {
        authService.logout(new AuthCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                // Clear preferences
                preferencesManager.clearUser();

                // Navigate to login screen
                Intent intent = new Intent(requireContext(), AuthActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                requireActivity().finish();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}