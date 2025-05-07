package com.example.traveladvisor360.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.traveladvisor360.fragments.CheminListFragment;

public class CheminPagerAdapter extends FragmentStateAdapter {

    private final int NUM_TABS = 3;

    public CheminPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Create a fragment for each tab
        return CheminListFragment.newInstance(position);
    }

    @Override
    public int getItemCount() {
        // Return a fixed number of tabs
        return NUM_TABS;
    }
}