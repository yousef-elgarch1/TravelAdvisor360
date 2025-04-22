package com.example.traveladvisor360.activities;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.example.traveladvisor360.R;
import com.example.traveladvisor360.adapters.TripPlanningPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class TripPlanningActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_planning);

        setupViewPager();
        setupTabs();

        // Get trip type from intent extras if available
        String tripType = getIntent().getStringExtra("trip_type");
        if (tripType != null) {
            // Handle trip type if passed from dialog
        }
    }

    private void setupViewPager() {
        viewPager = findViewById(R.id.view_pager);
        TripPlanningPagerAdapter adapter = new TripPlanningPagerAdapter(this);
        viewPager.setAdapter(adapter);
    }

    private void setupTabs() {
        tabLayout = findViewById(R.id.tab_layout);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Manual Planning");
                            break;
                        case 1:
                            tab.setText("AI Planning");
                            break;
                    }
                }
        ).attach();
    }
}