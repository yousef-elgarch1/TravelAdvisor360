package com.example.traveladvisor360.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.traveladvisor360.R;
import com.example.traveladvisor360.callbacks.AuthCallback;
import com.example.traveladvisor360.models.User;
import com.example.traveladvisor360.services.AuthService;
import com.example.traveladvisor360.utils.SharedPreferencesManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;

public class ProfileActivity extends AppCompatActivity {

    // Profile information
    private ShapeableImageView ivProfilePic;
    private FloatingActionButton fabEditProfilePic;
    private TextInputEditText etName, etEmail;
    private MaterialButton btnEditProfile, btnLogout;

    // Settings
    private SwitchMaterial switchOfflineMode, switchDarkMode, switchNotifications;

    // Services
    private AuthService authService;
    private SharedPreferencesManager preferencesManager;

    // State
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        authService = AuthService.getInstance(this);
        preferencesManager = SharedPreferencesManager.getInstance(this);

        initViews();
        loadUserProfile();
        setupClickListeners();
    }

    private void initViews() {
        // Try to find all views in the layout
        try {
            // Profile views
            ivProfilePic = findViewById(R.id.iv_profile_pic);
            fabEditProfilePic = findViewById(R.id.fab_edit_profile_pic);

            // If using TextView instead of EditText for name/email
            if (findViewById(R.id.tv_name) != null) {
                // Using TextView layout
                etName = null; // We don't have EditTexts in this layout
                etEmail = null;
            } else {
                // Using EditText layout
                etName = findViewById(R.id.et_name);
                etEmail = findViewById(R.id.et_email);
            }

            // Buttons
            btnEditProfile = findViewById(R.id.btn_edit_profile);
            btnLogout = findViewById(R.id.btn_logout);

            // Settings switches
            try { switchOfflineMode = findViewById(R.id.switch_offline_mode); } catch (Exception e) {}
            try { switchDarkMode = findViewById(R.id.switch_dark_mode); } catch (Exception e) {}
            try { switchNotifications = findViewById(R.id.switch_notifications); } catch (Exception e) {}

        } catch (Exception e) {
            Toast.makeText(this, "Error initializing views", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadUserProfile() {
        // Get user from shared preferences
        User currentUser = preferencesManager.getCurrentUser();

        if (currentUser != null) {
            // Display user info
            if (etName != null) {
                etName.setText(currentUser.getName());
            } else if (findViewById(R.id.tv_name) != null) {
                ((android.widget.TextView) findViewById(R.id.tv_name)).setText(currentUser.getName());
            }

            if (etEmail != null) {
                etEmail.setText(currentUser.getEmail());
            } else if (findViewById(R.id.tv_username) != null) {
                ((android.widget.TextView) findViewById(R.id.tv_username)).setText("@" + currentUser.getUsername());
            }

            // Load profile picture if available
            if (ivProfilePic != null && currentUser.getProfilePicUrl() != null && !currentUser.getProfilePicUrl().isEmpty()) {
                Glide.with(this)
                        .load(currentUser.getProfilePicUrl())
                        .circleCrop()
                        .placeholder(R.drawable.ic_profile)
                        .into(ivProfilePic);
            }

            // Set switches if they exist
            if (switchNotifications != null && currentUser.getPreferences() != null) {
                switchNotifications.setChecked(currentUser.getPreferences().isReceiveNotifications());
            }

            if (switchDarkMode != null) {
                switchDarkMode.setChecked(preferencesManager.isDarkMode());
            }

        } else {
            // Use placeholder data
            if (etName != null) {
                etName.setText("John Doe");
            } else if (findViewById(R.id.tv_name) != null) {
                ((android.widget.TextView) findViewById(R.id.tv_name)).setText("Olivia Smith");
            }

            if (etEmail != null) {
                etEmail.setText("john.doe@example.com");
            } else if (findViewById(R.id.tv_username) != null) {
                ((android.widget.TextView) findViewById(R.id.tv_username)).setText("@travelwith_olivia");
            }

            // Set bio if it exists
            if (findViewById(R.id.tv_bio) != null) {
                ((android.widget.TextView) findViewById(R.id.tv_bio)).setText(
                        "Passionate traveler exploring the world one city at a time. Budget-friendly trips are my specialty!");
            }

            // Set default switch values
            if (switchNotifications != null) switchNotifications.setChecked(true);
            if (switchDarkMode != null) switchDarkMode.setChecked(false);
            if (switchOfflineMode != null) switchOfflineMode.setChecked(true);
        }
    }

    private void setupClickListeners() {
        // Edit profile button
        if (btnEditProfile != null) {
            btnEditProfile.setOnClickListener(v -> {
                if (isEditMode) {
                    // Save profile changes
                    saveProfile();
                    isEditMode = false;
                    btnEditProfile.setText(R.string.edit_profile);

                    // Disable editing
                    if (etName != null) etName.setEnabled(false);
                    if (etEmail != null) etEmail.setEnabled(false);
                } else {
                    // Enable editing
                    isEditMode = true;
                    btnEditProfile.setText(R.string.save_profile);

                    // Enable fields for editing
                    if (etName != null) etName.setEnabled(true);
                    if (etEmail != null) etEmail.setEnabled(true);
                }
            });
        }

        // Profile picture edit button
        if (fabEditProfilePic != null) {
            fabEditProfilePic.setOnClickListener(v -> {
                Toast.makeText(this, "Change profile picture", Toast.LENGTH_SHORT).show();
                // TODO: Implement image selection
            });
        }

        // Logout button
        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> {
                authService.logout(new AuthCallback<Void>() {
                    @Override
                    public void onSuccess(Void result) {
                        // Clear preferences
                        preferencesManager.clearUser();

                        // Navigate to login screen
                        Intent intent = new Intent(ProfileActivity.this, AuthActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(ProfileActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                });
            });
        }

        // Settings switches
        if (switchDarkMode != null) {
            switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
                preferencesManager.setDarkMode(isChecked);
                Toast.makeText(this, "Dark mode will be applied on restart", Toast.LENGTH_SHORT).show();
            });
        }

        if (switchNotifications != null) {
            switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
                // Save notification preference
                User user = preferencesManager.getCurrentUser();
                if (user != null && user.getPreferences() != null) {
                    user.getPreferences().setReceiveNotifications(isChecked);
                    preferencesManager.saveUser(user);
                }
            });
        }

        if (switchOfflineMode != null) {
            switchOfflineMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
                // Save offline mode preference
                Toast.makeText(this,
                        isChecked ? "Offline mode enabled" : "Offline mode disabled",
                        Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void saveProfile() {
        // Get values from EditTexts if they exist
        String name = (etName != null) ? etName.getText().toString() : "";
        String email = (etEmail != null) ? etEmail.getText().toString() : "";

        // Update user in preferences
        User user = preferencesManager.getCurrentUser();
        if (user != null) {
            user.setName(name);
            user.setEmail(email);
            preferencesManager.saveUser(user);
        }

        // Show confirmation
        Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show();
    }
}