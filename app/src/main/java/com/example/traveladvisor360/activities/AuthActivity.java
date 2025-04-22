package com.example.traveladvisor360.activities;


import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.traveladvisor360.R;
import com.example.traveladvisor360.callbacks.AuthCallback;
import com.example.traveladvisor360.models.User;
import com.example.traveladvisor360.services.AuthService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;

public class AuthActivity extends AppCompatActivity {

    private TabLayout authTabs;
    private TextInputLayout emailLayout;
    private TextInputLayout passwordLayout;
    private TextInputLayout nameLayout;
    private TextInputLayout confirmPasswordLayout;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etName;
    private EditText etConfirmPassword;
    private MaterialButton btnAuth;
    private MaterialButton btnGoogle;
    private MaterialButton btnFacebook;
    private LinearProgressIndicator progressIndicator;
    private AuthService authService;

    private boolean isSignUpMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        authService = AuthService.getInstance(this);

        initViews();
        setupTabListener();
        setupClickListeners();
    }

    private void initViews() {
        authTabs = findViewById(R.id.auth_tabs);
        emailLayout = findViewById(R.id.email_layout);
        passwordLayout = findViewById(R.id.password_layout);
        nameLayout = findViewById(R.id.name_layout);
        confirmPasswordLayout = findViewById(R.id.confirm_password_layout);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etName = findViewById(R.id.et_name);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        btnAuth = findViewById(R.id.btn_auth);
        btnGoogle = findViewById(R.id.btn_google);
        btnFacebook = findViewById(R.id.btn_facebook);
        progressIndicator = findViewById(R.id.progress_indicator);

        nameLayout.setVisibility(View.GONE);
        confirmPasswordLayout.setVisibility(View.GONE);
    }

    private void setupTabListener() {
        authTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                isSignUpMode = tab.getPosition() == 1;
                updateUI();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void setupClickListeners() {
        btnAuth.setOnClickListener(v -> {
            if (validateInput()) {
                if (isSignUpMode) {
                    signUp();
                } else {
                    signIn();
                }
            }
        });

        btnGoogle.setOnClickListener(v -> signInWithGoogle());
        btnFacebook.setOnClickListener(v -> signInWithFacebook());
    }

    private void updateUI() {
        if (isSignUpMode) {
            btnAuth.setText(R.string.sign_up);
            nameLayout.setVisibility(View.VISIBLE);
            confirmPasswordLayout.setVisibility(View.VISIBLE);
        } else {
            btnAuth.setText(R.string.sign_in);
            nameLayout.setVisibility(View.GONE);
            confirmPasswordLayout.setVisibility(View.GONE);
        }

        etEmail.setText("");
        etPassword.setText("");
        etName.setText("");
        etConfirmPassword.setText("");

        emailLayout.setError(null);
        passwordLayout.setError(null);
        nameLayout.setError(null);
        confirmPasswordLayout.setError(null);
    }

    private boolean validateInput() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        boolean isValid = true;

        if (email.isEmpty()) {
            emailLayout.setError(getString(R.string.error_field_required));
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLayout.setError(getString(R.string.error_invalid_email));
            isValid = false;
        } else {
            emailLayout.setError(null);
        }

        if (password.isEmpty()) {
            passwordLayout.setError(getString(R.string.error_field_required));
            isValid = false;
        } else if (password.length() < 6) {
            passwordLayout.setError(getString(R.string.error_invalid_password));
            isValid = false;
        } else {
            passwordLayout.setError(null);
        }

        if (isSignUpMode) {
            String name = etName.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();

            if (name.isEmpty()) {
                nameLayout.setError(getString(R.string.error_field_required));
                isValid = false;
            } else {
                nameLayout.setError(null);
            }

            if (confirmPassword.isEmpty()) {
                confirmPasswordLayout.setError(getString(R.string.error_field_required));
                isValid = false;
            } else if (!confirmPassword.equals(password)) {
                confirmPasswordLayout.setError(getString(R.string.error_passwords_dont_match));
                isValid = false;
            } else {
                confirmPasswordLayout.setError(null);
            }
        }

        return isValid;
    }

    private void signIn() {
        showLoading(true);

        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        authService.login(email, password, new AuthCallback<User>() {
            @Override
            public void onSuccess(User result) {
                showLoading(false);
                Toast.makeText(AuthActivity.this, R.string.success_login, Toast.LENGTH_SHORT).show();
                navigateToMain();
            }

            @Override
            public void onError(String error) {
                showLoading(false);
                Toast.makeText(AuthActivity.this, error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void signUp() {
        showLoading(true);

        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String name = etName.getText().toString().trim();

        authService.register(email, password, name, new AuthCallback<User>() {
            @Override
            public void onSuccess(User result) {
                showLoading(false);
                Toast.makeText(AuthActivity.this, R.string.success_registration, Toast.LENGTH_SHORT).show();
                navigateToMain();
            }

            @Override
            public void onError(String error) {
                showLoading(false);
                Toast.makeText(AuthActivity.this, error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void signInWithGoogle() {
        Toast.makeText(this, "Google Sign-In not implemented", Toast.LENGTH_SHORT).show();
    }

    private void signInWithFacebook() {
        Toast.makeText(this, "Facebook Sign-In not implemented", Toast.LENGTH_SHORT).show();
    }

    private void showLoading(boolean show) {
        progressIndicator.setVisibility(show ? View.VISIBLE : View.GONE);
        btnAuth.setEnabled(!show);
        btnGoogle.setEnabled(!show);
        btnFacebook.setEnabled(!show);
    }

    private void navigateToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
