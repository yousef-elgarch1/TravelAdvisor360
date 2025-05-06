package com.example.traveladvisor360.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.traveladvisor360.R;
import com.example.traveladvisor360.callbacks.AuthCallback;
import com.example.traveladvisor360.services.AuthService;
import com.example.traveladvisor360.utils.SharedPreferencesManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class ProfileActivity extends AppCompatActivity {

    private TextInputEditText etName;
    private TextInputEditText etEmail;
    private MaterialButton btnEditProfile;
    private MaterialButton btnChangePassword;
    private MaterialButton btnLogout;
    private AuthService authService;
    private SharedPreferencesManager preferencesManager;

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
        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        btnEditProfile = findViewById(R.id.btn_edit_profile);
        btnChangePassword = findViewById(R.id.btn_change_password);
        btnLogout = findViewById(R.id.btn_logout);
    }

    private void loadUserProfile() {
        // Load user profile from API or local storage
        etName.setText("John Doe");
        etEmail.setText("john.doe@example.com");
    }

    private void setupClickListeners() {
        btnEditProfile.setOnClickListener(v -> {
            // Enable editing of fields
            etName.setEnabled(true);
            btnEditProfile.setText("Save Profile");
        });

        btnChangePassword.setOnClickListener(v -> {
            // Navigate to change password screen
        });

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
}