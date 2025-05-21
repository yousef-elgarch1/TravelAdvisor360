package com.example.traveladvisor360.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.traveladvisor360.R;
import com.example.traveladvisor360.activities.AuthActivity;
import com.example.traveladvisor360.callbacks.AuthCallback;
import com.example.traveladvisor360.models.User;
import com.example.traveladvisor360.network.ApiResponse;
import com.example.traveladvisor360.services.AuthService;
import com.example.traveladvisor360.utils.ApiClient;
import com.example.traveladvisor360.utils.SharedPreferencesManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;

public class ProfileFragment extends Fragment {

    // UI Components - Core profile info
    private ShapeableImageView ivProfilePic;
    private FloatingActionButton fabEditProfilePic;
    private TextView tvName, tvUsername, tvBio;
    private MaterialButton btnEditProfile, btnLogout;
    private TextInputEditText etName, etEmail;

    // Stats
    private TextView tvTripsCount, tvCountriesCount, tvPhotosCount;

    // Payment methods
    private ImageButton btnEditMastercard, btnEditPaypal;
    private MaterialButton btnAddPaymentMethod;

    // Social media
    private SwitchMaterial switchInstagram, switchFacebook;
    private MaterialButton btnConnectTwitter;

    // Settings
    private SwitchMaterial switchOfflineMode, switchDarkMode, switchNotifications;
    private View layoutLanguage;

    // Services
    private AuthService authService;
    private SharedPreferencesManager preferencesManager;
    private User currentUser;

    // State
    private boolean isEditMode = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Use the existing activity_profile.xml layout
        return inflater.inflate(R.layout.activity_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        authService = AuthService.getInstance(requireContext());
        preferencesManager = SharedPreferencesManager.getInstance(requireContext());

        // Initialize views - only find views that exist in your layout
        initViews(view);

        // Load user data from preferences or API
        loadUserProfile();

        // Set up listeners
        setupClickListeners();
    }

    private void initViews(View view) {
        try {
            // Core profile info - TextView version (in card layout)
            try {
                ivProfilePic = view.findViewById(R.id.iv_profile_pic);
                tvName = view.findViewById(R.id.tv_name);
                tvUsername = view.findViewById(R.id.tv_username);
                tvBio = view.findViewById(R.id.tv_bio);
                fabEditProfilePic = view.findViewById(R.id.fab_edit_profile_pic);
            } catch (Exception e) {
                // Some views might not exist
            }

            // Core profile info - EditText version (in original layout)
            try {
                etName = view.findViewById(R.id.et_name);
                etEmail = view.findViewById(R.id.et_email);
            } catch (Exception e) {
                // These fields might not exist
            }

            // Edit and logout buttons - should exist in both layouts
            btnEditProfile = view.findViewById(R.id.btn_edit_profile);
            btnLogout = view.findViewById(R.id.btn_logout);

            // Stats
            try {
                tvTripsCount = view.findViewById(R.id.tv_trips_completed_count);
                tvCountriesCount = view.findViewById(R.id.tv_countries_visited_count);
                tvPhotosCount = view.findViewById(R.id.tv_photos_uploaded_count);
            } catch (Exception e) {
                // These might not exist
            }

            // Payment methods
            try {
                btnEditMastercard = view.findViewById(R.id.btn_edit_mastercard);
                btnEditPaypal = view.findViewById(R.id.btn_edit_paypal);
                btnAddPaymentMethod = view.findViewById(R.id.btn_add_payment_method);
            } catch (Exception e) {
                // These might not exist
            }

            // Social media
            try {
                switchInstagram = view.findViewById(R.id.switch_instagram);
                switchFacebook = view.findViewById(R.id.switch_facebook);
                btnConnectTwitter = view.findViewById(R.id.btn_connect_twitter);
            } catch (Exception e) {
                // These might not exist
            }

            // Settings
            try {
                switchOfflineMode = view.findViewById(R.id.switch_offline_mode);
                switchDarkMode = view.findViewById(R.id.switch_dark_mode);
                switchNotifications = view.findViewById(R.id.switch_notifications);
                layoutLanguage = view.findViewById(R.id.layout_language);
            } catch (Exception e) {
                // These might not exist
            }

        } catch (Exception e) {
            // Handle exceptions gracefully to prevent crashes
            Toast.makeText(requireContext(), "Error initializing views: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void setupClickListeners() {
        // Edit profile button
        if (btnEditProfile != null) {
            btnEditProfile.setOnClickListener(v -> {
                if (isUsingEditTextLayout()) {
                    // Toggle edit mode if using EditText layout
                    if (isEditMode) {
                        saveUserProfile();
                        isEditMode = false;
                        btnEditProfile.setText(R.string.edit_profile);
                        toggleEditableFields(false);
                    } else {
                        isEditMode = true;
                        btnEditProfile.setText(R.string.save_profile);
                        toggleEditableFields(true);
                    }
                } else {
                    // Show dialog for card layout
                    showEditProfileDialog();
                }
            });
        }

        // Logout button
        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> {
                showLogoutConfirmationDialog();
            });
        }

        // Edit profile picture
        if (fabEditProfilePic != null) {
            fabEditProfilePic.setOnClickListener(v -> {
                Toast.makeText(requireContext(), "Change profile picture", Toast.LENGTH_SHORT).show();
                // TODO: Add image selection
            });
        }

        // Settings switches
        if (switchNotifications != null) {
            switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (currentUser != null && currentUser.getPreferences() != null) {
                    currentUser.getPreferences().setReceiveNotifications(isChecked);
                    // Save preference to server
                    updateUserPreferences();
                }
            });
        }

        if (switchDarkMode != null) {
            switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
                preferencesManager.setDarkMode(isChecked);
                Toast.makeText(requireContext(),
                        "Dark mode will be applied on restart",
                        Toast.LENGTH_SHORT).show();
            });
        }

        if (switchOfflineMode != null) {
            switchOfflineMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
                Toast.makeText(requireContext(),
                        isChecked ? "Offline mode enabled" : "Offline mode disabled",
                        Toast.LENGTH_SHORT).show();
            });
        }

        // Social media switches
        if (switchInstagram != null) {
            switchInstagram.setOnCheckedChangeListener((buttonView, isChecked) -> {
                Toast.makeText(requireContext(),
                        isChecked ? "Connected to Instagram" : "Disconnected from Instagram",
                        Toast.LENGTH_SHORT).show();
            });
        }

        if (switchFacebook != null) {
            switchFacebook.setOnCheckedChangeListener((buttonView, isChecked) -> {
                Toast.makeText(requireContext(),
                        isChecked ? "Connected to Facebook" : "Disconnected from Facebook",
                        Toast.LENGTH_SHORT).show();
            });
        }

        // Connect Twitter button
        if (btnConnectTwitter != null) {
            btnConnectTwitter.setOnClickListener(v -> {
                Toast.makeText(requireContext(), "Connecting to Twitter...", Toast.LENGTH_SHORT).show();
            });
        }

        // Language setting
        if (layoutLanguage != null) {
            layoutLanguage.setOnClickListener(v -> {
                Toast.makeText(requireContext(), "Language settings", Toast.LENGTH_SHORT).show();
            });
        }

        // Payment methods
        if (btnAddPaymentMethod != null) {
            btnAddPaymentMethod.setOnClickListener(v -> {
                Toast.makeText(requireContext(), "Add payment method", Toast.LENGTH_SHORT).show();
            });
        }

        if (btnEditMastercard != null) {
            btnEditMastercard.setOnClickListener(v -> {
                Toast.makeText(requireContext(), "Edit Mastercard", Toast.LENGTH_SHORT).show();
            });
        }

        if (btnEditPaypal != null) {
            btnEditPaypal.setOnClickListener(v -> {
                Toast.makeText(requireContext(), "Edit PayPal", Toast.LENGTH_SHORT).show();
            });
        }
    }

    private boolean isUsingEditTextLayout() {
        // Check if we're using the layout with EditText fields
        return etName != null && etEmail != null;
    }

    private void toggleEditableFields(boolean editable) {
        if (etName != null) etName.setEnabled(editable);
        if (etEmail != null) etEmail.setEnabled(editable);
    }

    private void loadUserProfile() {
        // Get user from shared preferences
        currentUser = preferencesManager.getCurrentUser();

        if (currentUser != null) {
            displayUserData(currentUser);
        } else {
            // Call API to get current user
            try {
                ApiClient.getInstance().getApiService().getCurrentUser().enqueue(new retrofit2.Callback<ApiResponse<User>>() {
                    @Override
                    public void onResponse(retrofit2.Call<ApiResponse<User>> call, retrofit2.Response<ApiResponse<User>> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            currentUser = response.body().getData();
                            preferencesManager.saveUser(currentUser);
                            displayUserData(currentUser);
                        } else {
                            // Handle error but use placeholder data for now
                            displayPlaceholderData();
                        }
                    }

                    @Override
                    public void onFailure(retrofit2.Call<ApiResponse<User>> call, Throwable t) {
                        // Handle error but use placeholder data
                        displayPlaceholderData();
                    }
                });
            } catch (Exception e) {
                // If API client not set up, use placeholder
                displayPlaceholderData();
            }
        }
    }

    private void displayUserData(User user) {
        // Display user data in the UI based on which layout we're using
        if (isUsingEditTextLayout()) {
            // EditText layout
            if (etName != null) etName.setText(user.getName());
            if (etEmail != null) etEmail.setText(user.getEmail());
        } else {
            // Card layout with TextViews
            if (tvName != null) tvName.setText(user.getName());
            if (tvUsername != null) {
                try {
                    tvUsername.setText("@" + user.getUsername());
                } catch (Exception e) {
                    // Username might not be available
                    tvUsername.setText("@" + user.getName().toLowerCase().replace(" ", "_"));
                }
            }
            if (tvBio != null) {
                try {
                    tvBio.setText(user.getBio());
                } catch (Exception e) {
                    // Bio might not be available
                    tvBio.setText("Traveler");
                }
            }
        }

        // Load profile picture with Glide if available
        if (ivProfilePic != null && user.getProfilePicUrl() != null && !user.getProfilePicUrl().isEmpty()) {
            try {
                Glide.with(this)
                        .load(user.getProfilePicUrl())
                        .circleCrop()
                        .placeholder(R.drawable.ic_profile)
                        .into(ivProfilePic);
            } catch (Exception e) {
                // If Glide fails, keep the placeholder
            }
        }

        // Set switch states if they exist
        if (switchNotifications != null && user.getPreferences() != null) {
            switchNotifications.setChecked(user.getPreferences().isReceiveNotifications());
        }

        if (switchDarkMode != null) {
            switchDarkMode.setChecked(preferencesManager.isDarkMode());
        }

        // Set stats if they exist
        try {
            if (tvTripsCount != null) tvTripsCount.setText(String.valueOf(user.getTripsCount()));
            if (tvCountriesCount != null) tvCountriesCount.setText(String.valueOf(user.getCountriesCount()));
            if (tvPhotosCount != null) tvPhotosCount.setText(String.valueOf(user.getPhotosCount()));
        } catch (Exception e) {
            // These might not be available in the User model
            if (tvTripsCount != null) tvTripsCount.setText("0");
            if (tvCountriesCount != null) tvCountriesCount.setText("0");
            if (tvPhotosCount != null) tvPhotosCount.setText("0");
        }
    }

    private void displayPlaceholderData() {
        // Display placeholder data when we can't get real user data
        if (isUsingEditTextLayout()) {
            // EditText layout
            if (etName != null) etName.setText("John Doe");
            if (etEmail != null) etEmail.setText("john.doe@example.com");
        } else {
            // Card layout with TextViews
            if (tvName != null) tvName.setText("Olivia Smith");
            if (tvUsername != null) tvUsername.setText("@travelwith_olivia");
            if (tvBio != null) tvBio.setText(
                    "Passionate traveler exploring the world one city at a time. Budget-friendly trips are my specialty!"
            );
        }

        // Set stats if they exist
        if (tvTripsCount != null) tvTripsCount.setText("12");
        if (tvCountriesCount != null) tvCountriesCount.setText("8");
        if (tvPhotosCount != null) tvPhotosCount.setText("150");

        // Set switches to default values
        if (switchNotifications != null) switchNotifications.setChecked(true);
        if (switchDarkMode != null) switchDarkMode.setChecked(false);
        if (switchOfflineMode != null) switchOfflineMode.setChecked(true);
        if (switchInstagram != null) switchInstagram.setChecked(true);
        if (switchFacebook != null) switchFacebook.setChecked(false);
    }

    private void saveUserProfile() {
        if (!isUsingEditTextLayout()) return;

        // Get updated values from form
        String name = etName != null ? etName.getText().toString() : "";
        String email = etEmail != null ? etEmail.getText().toString() : "";

        // Update current user
        if (currentUser != null) {
            currentUser.setName(name);
            currentUser.setEmail(email);

            // Save to SharedPreferences
            preferencesManager.saveUser(currentUser);

            // Save to API
            updateUserProfile();
        }

        // Disable editing
        toggleEditableFields(false);
        Toast.makeText(requireContext(), "Profile updated", Toast.LENGTH_SHORT).show();
    }

    private void updateUserProfile() {
        // Update user profile on the server
        try {
            ApiClient.getInstance().getApiService().updateUser(currentUser).enqueue(
                    new retrofit2.Callback<ApiResponse<User>>() {
                        @Override
                        public void onResponse(retrofit2.Call<ApiResponse<User>> call,
                                               retrofit2.Response<ApiResponse<User>> response) {
                            if (response.isSuccessful() && response.body() != null &&
                                    response.body().isSuccess()) {
                                // Update was successful
                                Toast.makeText(requireContext(),
                                        "Profile updated successfully", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(retrofit2.Call<ApiResponse<User>> call, Throwable t) {
                            // Handle network error silently - we've already saved to SharedPreferences
                        }
                    }
            );
        } catch (Exception e) {
            // API client might not be properly set up
        }
    }

    private void updateUserPreferences() {
        // Update user preferences on the server
        // This would typically be a separate API call
    }

    private void showEditProfileDialog() {
        // Show dialog to edit profile for the card layout
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Edit Profile")
                .setMessage("Profile editing is currently only available in the app's settings.")
                .setPositiveButton("OK", null)
                .show();
    }

    private void showLogoutConfirmationDialog() {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Logout")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Yes", (dialog, which) -> performLogout())
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void performLogout() {
        authService.logout(new AuthCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                // Clear user data
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