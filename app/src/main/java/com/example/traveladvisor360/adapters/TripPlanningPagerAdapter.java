package com.example.traveladvisor360.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.traveladvisor360.fragments.AIPlanningFragment;
import com.example.traveladvisor360.fragments.ManualPlanningFragment;

public class TripPlanningPagerAdapter extends FragmentStateAdapter {

    public TripPlanningPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new ManualPlanningFragment();
            case 1:
                return new AIPlanningFragment();
            default:
                return new ManualPlanningFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2; // Manual Planning and AI Planning
    }
}