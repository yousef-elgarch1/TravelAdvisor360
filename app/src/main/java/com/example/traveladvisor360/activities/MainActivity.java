package com.example.traveladvisor360.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.traveladvisor360.R;
import com.example.traveladvisor360.fragments.PlanningOptionsDialog;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private NavController navController;
    private BottomNavigationView bottomNavigationView;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FloatingActionButton fabAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Initialize drawer layout
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Setup drawer toggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();



        // Setup notification icon click
        ImageView notificationIcon = findViewById(R.id.notification_icon);
        if (notificationIcon != null) {
            notificationIcon.setOnClickListener(v -> {
                showToast("Notifications");
            });
        }

        // Setup profile header icon click
        ImageView profileIcon = findViewById(R.id.profile_icon);
        if (profileIcon != null) {
            profileIcon.setOnClickListener(v -> {
                navigateToDestination(R.id.profileFragment);
            });
        }

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
        bottomNavigationView.setOnItemSelectedListener(this::onBottomNavigationItemSelected);

        // Setup floating action button
        fabAdd = findViewById(R.id.fab_add);
        if (fabAdd != null) {
            fabAdd.setOnClickListener(v ->
                showTripPlanningDialog());
        }

        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
    }

    private void showTripPlanningDialog() {
        PlanningOptionsDialog dialog = new PlanningOptionsDialog();
        dialog.show(getSupportFragmentManager(), "PlanningOptions");
    }



    private boolean onBottomNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.navigation_home) {
            navigateToDestination(R.id.homeFragment);
            return true;
        } else if (itemId == R.id.navigation_destinations) {
            navigateToDestination(R.id.destinationsFragment);
            return true;
        } else if (itemId == R.id.navigation_chemin) {
            navigateToDestination(R.id.itinerariesFragment);
            return true;
        } else if (itemId == R.id.navigation_experiences) {
            navigateToDestination(R.id.experiencesFragment);
            return true;
        } else if (itemId == R.id.navigation_profile) {
            navigateToDestination(R.id.profileFragment);
            return true;
        }

        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            navigateToDestination(R.id.homeFragment);
        } else if (id == R.id.nav_my_chemin) {
            navigateToDestination(R.id.itinerariesFragment);
        } else if (id == R.id.nav_favorites) {
            showToast("Favoris");
        } else if (id == R.id.nav_settings) {
            showToast("Paramètres");
        } else if (id == R.id.nav_help) {
            showToast("Aide & Support");
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void navigateToDestination(int destinationId) {
        NavOptions navOptions = new NavOptions.Builder()
                .setLaunchSingleTop(true)
                .setPopUpTo(navController.getGraph().getStartDestinationId(), false)
                .build();
        navController.navigate(destinationId, null, navOptions);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }
}