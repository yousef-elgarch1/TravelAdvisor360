package com.example.traveladvisor360.activities;



import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.traveladvisor360.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private NavController navController;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize bottom navigation
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Setup with NavController
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
            NavigationUI.setupWithNavController(bottomNavigationView, navController);
        }

        // Set listener for navigation item selection
        bottomNavigationView.setOnItemSelectedListener(this::onNavigationItemSelected);

        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
    }

    private boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.navigation_home) {
            navController.navigate(R.id.homeFragment);
            return true;
        } else if (itemId == R.id.navigation_destinations) {
            navController.navigate(R.id.destinationsFragment);
            return true;
        } else if (itemId == R.id.navigation_itineraries) {
            navController.navigate(R.id.itinerariesFragment);
            return true;
        } else if (itemId == R.id.navigation_experiences) {
            navController.navigate(R.id.experiencesFragment);
            return true;
        } else if (itemId == R.id.navigation_profile) {
            navController.navigate(R.id.profileFragment);
            return true;
        }

        return false;
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }
}