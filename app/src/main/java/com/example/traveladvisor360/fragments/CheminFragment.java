package com.example.traveladvisor360.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import com.example.traveladvisor360.R;
import com.example.traveladvisor360.adapters.CheminPagerAdapter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class CheminFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private CheminPagerAdapter pagerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chemin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.view_pager);
        MaterialButton btnNewChemin = view.findViewById(R.id.btn_new_chemin);

        pagerAdapter = new CheminPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        // Connect TabLayout with ViewPager2
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText(R.string.a_venir);
                    break;
                case 1:
                    tab.setText(R.string.en_cours);
                    break;
                case 2:
                    tab.setText(R.string.termines);
                    break;
            }
        }).attach();

        // Setup new chemin button
        btnNewChemin.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.action_cheminFragment_to_newCheminFragment);
        });
    }
}