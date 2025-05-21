package com.example.traveladvisor360.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.traveladvisor360.R;
import com.example.traveladvisor360.adapters.ItineraryAdapter;
import com.example.traveladvisor360.database.ItineraryRepository;
import com.example.traveladvisor360.models.SavedItinerary;
import com.example.traveladvisor360.utils.SharedPreferencesManager;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ItinerariesFragment extends Fragment {
    private RecyclerView recyclerView;
    private TextView emptyView;
    private ChipGroup filterChipGroup;
    private ItineraryAdapter adapter;
    private ItineraryRepository itineraryRepository;
    private List<SavedItinerary> allItineraries;
    private String currentUserId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_itineraries, container, false);
        
        recyclerView = view.findViewById(R.id.recyclerView);
        emptyView = view.findViewById(R.id.emptyView);
        filterChipGroup = view.findViewById(R.id.filterChipGroup);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ItineraryAdapter(requireContext());
        recyclerView.setAdapter(adapter);

        itineraryRepository = ItineraryRepository.getInstance(requireContext());
        currentUserId = SharedPreferencesManager.getInstance(requireContext()).getCurrentUserId();
        setupFilterChips();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadItineraries();
    }

    private void setupFilterChips() {
        filterChipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.chipAll) {
                showAllItineraries();
            } else if (checkedId == R.id.chipUpcoming) {
                filterItinerariesByStatus("upcoming");
            } else if (checkedId == R.id.chipCurrent) {
                filterItinerariesByStatus("current");
            } else if (checkedId == R.id.chipPast) {
                filterItinerariesByStatus("past");
            }
        });
    }

    private void loadItineraries() {
        // Get current user ID from SharedPreferences
        currentUserId = SharedPreferencesManager.getInstance(requireContext()).getCurrentUserId();
        if (currentUserId.isEmpty()) {
            // Handle case where user is not logged in
            Toast.makeText(requireContext(), "Please log in to view your itineraries", Toast.LENGTH_SHORT).show();
            return;
        }

        // Load itineraries from database
        allItineraries = itineraryRepository.getAllItineraries(currentUserId);
        updateAdapter(allItineraries);
    }

    private void showAllItineraries() {
        updateAdapter(allItineraries);
    }

    private void filterItinerariesByStatus(String status) {
        List<SavedItinerary> filteredList = new ArrayList<>();
        Date now = new Date();

        for (SavedItinerary itinerary : allItineraries) {
            switch (status) {
                case "upcoming":
                    if (itinerary.isUpcoming()) {
                        filteredList.add(itinerary);
                    }
                    break;
                case "current":
                    if (itinerary.isCurrent()) {
                        filteredList.add(itinerary);
                    }
                    break;
                case "past":
                    if (itinerary.isPast()) {
                        filteredList.add(itinerary);
                    }
                    break;
            }
        }

        updateAdapter(filteredList);
    }

    private void updateAdapter(List<SavedItinerary> itineraries) {
        adapter.setSavedItineraries(itineraries);
        emptyView.setVisibility(itineraries.isEmpty() ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(itineraries.isEmpty() ? View.GONE : View.VISIBLE);
    }
} 