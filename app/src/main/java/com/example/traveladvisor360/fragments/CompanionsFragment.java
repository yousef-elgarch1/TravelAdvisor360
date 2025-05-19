package com.example.traveladvisor360.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.traveladvisor360.R;
import com.example.traveladvisor360.adapters.TravelCompanionAdapter;
import com.example.traveladvisor360.models.TravelCompanion;
import com.example.traveladvisor360.models.TripPlanningData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class CompanionsFragment extends Fragment {

    private RecyclerView companionsRecyclerView;
    private FloatingActionButton fabAddCompanion;
    private Button nextButton;
    private Button backButton;
    private TextView emptyStateTextView;

    private TripPlanningData tripData;
    private TravelCompanionAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tripData = TripPlanningData.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_companions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        companionsRecyclerView = view.findViewById(R.id.recycler_companions);
        fabAddCompanion = view.findViewById(R.id.fab_add_companion);
        nextButton = view.findViewById(R.id.btn_next);
        backButton = view.findViewById(R.id.btn_back);
        emptyStateTextView = view.findViewById(R.id.text_empty_companions);

        // Set up RecyclerView
        setupCompanionsRecyclerView();

        // Set up click listeners
        fabAddCompanion.setOnClickListener(v -> showAddCompanionDialog());

        nextButton.setOnClickListener(v -> {
            if (validateCompanions()) {
                navigateToActivitiesSelection();
            }
        });

        backButton.setOnClickListener(v -> {
            Navigation.findNavController(view).navigateUp();
        });

        // Update UI based on current data
        updateEmptyState();
    }

    private void setupCompanionsRecyclerView() {
        List<TravelCompanion> companions = tripData.getCompanions();
        adapter = new TravelCompanionAdapter(companions);
        companionsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        companionsRecyclerView.setAdapter(adapter);

        // Set item removed callback
        adapter.setOnCompanionRemovedListener(position -> {
            tripData.getCompanions().remove(position);
            updateEmptyState();
        });
    }

    private void showAddCompanionDialog() {
        AddCompanionDialog dialog = new AddCompanionDialog();
        dialog.setCompanionAddListener(companion -> {
            tripData.getCompanions().add(companion);
            adapter.notifyItemInserted(tripData.getCompanions().size() - 1);
            updateEmptyState();
        });
        dialog.show(getChildFragmentManager(), "AddCompanion");
    }

    private boolean validateCompanions() {
        if (tripData.getCompanions().isEmpty()) {
            Snackbar.make(requireView(), "Please add at least one travel companion", Snackbar.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void navigateToActivitiesSelection() {
        Navigation.findNavController(requireView())
                .navigate(R.id.action_companionsFragment_to_activitiesFragment);
    }

    private void updateEmptyState() {
        if (tripData.getCompanions().isEmpty()) {
            emptyStateTextView.setVisibility(View.VISIBLE);
            companionsRecyclerView.setVisibility(View.GONE);
        } else {
            emptyStateTextView.setVisibility(View.GONE);
            companionsRecyclerView.setVisibility(View.VISIBLE);
        }
    }
}