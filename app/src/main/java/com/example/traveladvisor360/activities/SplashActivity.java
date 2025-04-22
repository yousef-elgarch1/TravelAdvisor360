package com.example.traveladvisor360.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.traveladvisor360.R;
import com.example.traveladvisor360.services.AuthService;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_DELAY = 2000; // 2 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Hide the action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Initialize views
        ImageView logo = findViewById(R.id.iv_logo);
        TextView appName = findViewById(R.id.tv_app_name);

        // Start animations
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        logo.startAnimation(fadeIn);
        appName.startAnimation(fadeIn);

        // Delay and navigate
        new Handler().postDelayed(() -> {
            // Check if user is logged in
            AuthService authService = AuthService.getInstance(this);
            Intent intent;

            if (authService.isLoggedIn()) {
                intent = new Intent(SplashActivity.this, MainActivity.class);
            } else {
                intent = new Intent(SplashActivity.this, AuthActivity.class);
            }

            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }, SPLASH_DELAY);
    }
}